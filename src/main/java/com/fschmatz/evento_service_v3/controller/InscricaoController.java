package com.fschmatz.evento_service_v3.controller;

import com.fschmatz.evento_service_v3.entity.*;
import com.fschmatz.evento_service_v3.repository.EventoRepository;
import com.fschmatz.evento_service_v3.repository.InscricaoRepository;
import com.fschmatz.evento_service_v3.repository.UsuarioRepository;
import com.fschmatz.evento_service_v3.util.Encrypt;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
@Transactional
@RequestMapping("/evento/inscricao")
public class InscricaoController {

    InscricaoRepository inscricaoRepository;
    EventoRepository eventoRepository;
    UsuarioRepository usuarioRepository;

    //http://localhost:9092/evento/inscricao/listarInscricoes
    @RequestMapping("/listarInscricoes")
    public ModelAndView listarInscricoes() {
        ModelAndView mv = new ModelAndView("listarTodasInscricoes");
        Iterable<Inscricao> inscricaos = inscricaoRepository.findAll();
        mv.addObject("inscricaos", inscricaos);
        return mv;
    }


    @RequestMapping("/imprimirCertificado/{idUsuario}/{idEvento}")
    public ModelAndView certificado(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idEvento") Integer idEvento) {

        String nomeUsuarioCertificado = usuarioRepository.getById(idUsuario).getNome();
        String nomeEventoCertificado = eventoRepository.getById(idEvento).getNome();
        String dataEventoCertificado = eventoRepository.getById(idEvento).getData();
        Certificado certificado = new Certificado();

        enviarEmailCertificado(nomeUsuarioCertificado, usuarioRepository.getById(idUsuario).getEmail(), nomeEventoCertificado);

        ModelAndView mv = new ModelAndView("pgCertificado");

        certificado.setNomeUsuario(nomeUsuarioCertificado);
        certificado.setNomeEvento(nomeEventoCertificado);
        certificado.setDataEvento(dataEventoCertificado);
        certificado.setCargaHoraria("120 Minutos");

        mv.addObject("certificado", certificado);
        return mv;
    }

    //http://localhost:9092/inscricao/listarInscricoesUsuario/1
    @RequestMapping("/listarInscricoesUsuario/{id}")
    public ModelAndView listarInscricoesUsuario(@PathVariable("id") Integer id) {

        //SOMENTE com checkin = 0
        Usuario usuarioSite = new Usuario();
        usuarioSite.setId_usuario(id);
        ModelAndView mv = new ModelAndView("listarInscricoes");
        Iterable<Inscricao> inscricaos = inscricaoRepository.findInscricaoByIdUsuarioAndCheckinEquals(usuarioSite, 0);
        mv.addObject("inscricaos", inscricaos);
        return mv;
    }

    @RequestMapping("/listarInscricoesUsuarioComCheckin/{id}")
    public ModelAndView listarInscricoesUsuarioComCheckin(@PathVariable("id") Integer id) {

        //SOMENTE com checkin = 1
        Usuario usuarioSite = new Usuario();
        usuarioSite.setId_usuario(id);
        ModelAndView mv = new ModelAndView("listarInscricoesComCheckIn");
        Iterable<Inscricao> inscricaos = inscricaoRepository.findInscricaoByIdUsuarioAndCheckinEquals(usuarioSite, 1);
        mv.addObject("inscricaos", inscricaos);

        return mv;
    }

    @GetMapping
    public ResponseEntity<List<Inscricao>> getAll() {
        try {
            List<Inscricao> items = new ArrayList<Inscricao>();
            inscricaoRepository.findAll().forEach(items::add);
            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/cancelarInscricao/{idUsuario}/{idInscricao}", method = RequestMethod.GET)
    public String cancelarInscricao(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idInscricao") Integer idInscricao) {

        String emailUsuario = usuarioRepository.getById(idUsuario).getEmail();
        Inscricao savedItem = inscricaoRepository.getById(idInscricao);
        Evento eventoData = eventoRepository.getById(savedItem.getIdEvento().getId_evento());

        if (calcularCancelamento(eventoData.getData())) {
            enviarEmail("Usuario", emailUsuario, "Sua inscri????o no evento " + eventoData.getNome() + " foi cancelada.");
            inscricaoRepository.deleteById(idInscricao);
            return "redirect:http://localhost:9090/usuario/homeUsuario/" + idUsuario;

        } else {
            return "errorCancel";
        }
    }

    @RequestMapping(value = "/fazerInscricao/{idUsuario}/{idEvento}", method = RequestMethod.GET)
    public String fazerInscricao(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idEvento") Integer idEvento) {

        Inscricao savedItem = new Inscricao();
        Evento savedEvento = new Evento();
        savedEvento.setId_evento(idEvento);
        Usuario savedUsuario = new Usuario();
        savedUsuario.setId_usuario(idUsuario);
        String emailUsuario = usuarioRepository.getById(idUsuario).getEmail();
        String nomeEventoEmail = eventoRepository.getById(idEvento).getNome();

        savedItem.setIdUsuario(savedUsuario);
        savedItem.setIdEvento(savedEvento);
        savedItem.setCheckin(0);
        savedItem.setData(getDataDiaAtual());
        inscricaoRepository.save(savedItem);
        enviarEmail("Usuario", emailUsuario, "Sua inscri????o no evento " + nomeEventoEmail + " foi confirmada.");

        return "redirect:http://localhost:9090/usuario/homeUsuario/" + idUsuario;
    }

    @RequestMapping(value = "/fazerCheckin/{idUsuario}/{idEvento}", method = RequestMethod.GET)
    public String fazerCheckin(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idEvento") Integer idEvento) {

        Inscricao savedItem = new Inscricao();
        Evento savedEvento = new Evento();
        savedEvento.setId_evento(idEvento);
        Usuario savedUsuario = new Usuario();
        savedUsuario.setId_usuario(idUsuario);
        savedItem.setIdUsuario(savedUsuario);
        savedItem.setIdEvento(savedEvento);
        savedItem.setCheckin(1);
        savedItem.setData(getDataDiaAtual());

        String emailUsuario = usuarioRepository.getById(idUsuario).getEmail();

        //check para ver se existe, sen??o update
        System.out.println((inscricaoRepository.countByIdUsuarioAndIdEvento(savedUsuario, savedEvento)).toString());
        if (inscricaoRepository.countByIdUsuarioAndIdEvento(savedUsuario, savedEvento) == 0) {
            inscricaoRepository.save(savedItem);
            System.out.println("SALVOU NOVO");
        } else {
            inscricaoRepository.updateCheckIn(savedEvento, savedUsuario);
            System.out.println("UPDATE");
        }
        enviarEmail("Usuario", emailUsuario, "Voc?? fez Check-In no Evento");

        return "pgAcaoCompleta";
    }

    @PostMapping
    public ResponseEntity<Inscricao> create(@RequestBody Inscricao inscricao) {
        try {
            Inscricao savedItem = inscricaoRepository.save(inscricao);
            System.out.println(savedItem.toString());
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/certificado/{idUsuario}/{idEvento}", method = RequestMethod.GET)
    public String requisitarCertificado(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idEvento") Integer idEvento) {

        String usuario = usuarioRepository.getById(idUsuario).getNome();
        String evento = eventoRepository.getById(idEvento).getNome();
        String emailUsuario = usuarioRepository.getById(idUsuario).getEmail();

        System.out.println("User -> " + usuario + " Email -> " + emailUsuario + " EV -> " + evento);
        enviarEmailCertificado(usuario, emailUsuario, evento);

        return "redirect:http://localhost:9090/usuario/homeUsuario/" + idUsuario;
    }


// alterar depois pra uma classe util
//----------------------####


    String getDataDiaAtual() {
        Date dataAtual = new Date();
        String dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataAtual);
        return dataFormatada;
    }

    boolean calcularCancelamento(String dataEvento) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(dataEvento);
            d2 = df.parse(getDataDiaAtual());
        } catch (java.text.ParseException evt) {
        }
        long dt = (d2.getTime() - d1.getTime()) + 3600000;
        long dias = (dt / 86400000L);

        if (dt / 86400000L <= 2 && dt / 86400000L >= 0) {
            return false;
        } else {
            return true;
        }
    }

    //http://localhost:9090/email/send/
    private String enviarEmail(String nome, String email, String msg) {

        String uri = "http://localhost:9090/email/send/" + nome + "/" + email + "/" + msg;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    private String enviarEmailCertificado(String nome, String email, String evento) {

        String msgCripto = Encrypt.encriptar("O certificado do Evento " + evento + " ?? valido.");

        System.out.println(msgCripto);

        String msg = "Ol?? " + nome + "\n\nC??digo de valida????o do certificado:\n\n" + msgCripto + "\n\nAtt\nFschmatz Eventos LLC";
        String uri = "http://localhost:9090/email/send/" + nome + "/" + email + "/" + msg;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    //APP SYNC
    //http://localhost:9092/evento/inscricao/listarInscricoes
    @RequestMapping(value = "/syncInscricao", method = RequestMethod.POST)
    public Usuario saveSyncUser(InscricaoSync InscricaoSync) {

        // preciso montar o inscricao aqui, vai vir o cpf tbm junta com essa inscricaoSync
        // Usar o cpf para requisitar o id do usuario e
        // ent??o criar a inscricao normal com ele
        System.out.println(InscricaoSync.toString());

        Usuario usuarioCpf = usuarioRepository.getByCpf(InscricaoSync.getCpf_user());
        Evento eventoSave = new Evento();
        eventoSave.setId_evento(InscricaoSync.getId_evento());
        Inscricao novaInscricao = new Inscricao();

        novaInscricao.setIdUsuario(usuarioCpf);
        novaInscricao.setIdEvento(eventoSave);
        novaInscricao.setData(InscricaoSync.getData());
        novaInscricao.setCheckin(InscricaoSync.getCheckin());

        novaInscricao.toString();

        Inscricao savedItem = inscricaoRepository.save(novaInscricao);

        return null;
    }

}
