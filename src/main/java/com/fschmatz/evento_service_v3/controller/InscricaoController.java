package com.fschmatz.evento_service_v3.controller;

import com.fschmatz.evento_service_v3.entity.Evento;
import com.fschmatz.evento_service_v3.entity.Inscricao;
import com.fschmatz.evento_service_v3.entity.Usuario;
import com.fschmatz.evento_service_v3.repository.EventoRepository;
import com.fschmatz.evento_service_v3.repository.InscricaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@Transactional
@RequestMapping("/evento/inscricao")
public class InscricaoController {

    InscricaoRepository inscricaoRepository;
    EventoRepository eventoRepository;

    //http://localhost:9092/evento/inscricao/listarInscricoes
    @RequestMapping("/listarInscricoes")
    public ModelAndView listarInscricoes(){
        ModelAndView mv = new ModelAndView("listarTodasInscricoes");
        Iterable<Inscricao> inscricaos = inscricaoRepository.findAll();
        mv.addObject("inscricaos", inscricaos);
        return mv;
    }

    //http://localhost:9092/inscricao/listarInscricoesUsuario/1
    @RequestMapping("/listarInscricoesUsuario/{id}")
    public ModelAndView listarInscricoesUsuario(@PathVariable("id") Integer id){

        //SOMENTE com checkin = 0
        Usuario usuarioSite = new Usuario();
        usuarioSite.setId_usuario(id);
        ModelAndView mv = new ModelAndView("listarInscricoes");
        //Iterable<Inscricao> inscricaos = inscricaoRepository.findInscricaoByIdUsuario(usuarioSite);
        Iterable<Inscricao> inscricaos = inscricaoRepository.findInscricaoByIdUsuarioAndCheckinEquals(usuarioSite,0);
        mv.addObject("inscricaos", inscricaos);
        return mv;
    }

    @RequestMapping("/listarInscricoesUsuarioComCheckin/{id}")
    public ModelAndView listarInscricoesUsuarioComCheckin(@PathVariable("id") Integer id){

        //SOMENTE com checkin = 1
        Usuario usuarioSite = new Usuario();
        usuarioSite.setId_usuario(id);
        ModelAndView mv = new ModelAndView("listarInscricoesComCheckIn");
        Iterable<Inscricao> inscricaos = inscricaoRepository.findInscricaoByIdUsuarioAndCheckinEquals(usuarioSite,1);
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
    public String cancelarInscricao(@PathVariable("idUsuario") Integer idUsuario,@PathVariable("idInscricao") Integer idInscricao) {
        inscricaoRepository.deleteById(idInscricao);
        return "redirect:http://localhost:9090/usuario/homeUsuario/"+idUsuario;
    }

    @RequestMapping(value = "/fazerInscricao/{idUsuario}/{idEvento}", method = RequestMethod.GET)
    public String fazerInscricao(@PathVariable("idUsuario") Integer idUsuario,@PathVariable("idEvento") Integer idEvento) {

        Inscricao savedItem = new Inscricao();
        Evento savedEvento = new Evento();
        savedEvento.setId_evento(idEvento);
        Usuario savedUsuario = new Usuario();
        savedUsuario.setId_usuario(idUsuario);

        savedItem.setIdUsuario(savedUsuario);
        savedItem.setIdEvento(savedEvento);
        savedItem.setCheckin(0);
        savedItem.setData("21/11/2021");
        inscricaoRepository.save(savedItem);
        return "redirect:http://localhost:9090/usuario/homeUsuario/"+idUsuario;
    }

    @RequestMapping(value = "/fazerCheckin/{idUsuario}/{idEvento}", method = RequestMethod.GET)
    public String fazerCheckin(@PathVariable("idUsuario") Integer idUsuario,@PathVariable("idEvento") Integer idEvento) {

        Inscricao savedItem = new Inscricao();
        Evento savedEvento = new Evento();
        savedEvento.setId_evento(idEvento);
        Usuario savedUsuario = new Usuario();
        savedUsuario.setId_usuario(idUsuario);
        savedItem.setIdUsuario(savedUsuario);
        savedItem.setIdEvento(savedEvento);
        savedItem.setCheckin(1);
        savedItem.setData("21/11/2021");
        inscricaoRepository.save(savedItem);

        return "teste";
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

}
