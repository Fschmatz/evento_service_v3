package com.fschmatz.evento_service_v3.controller;

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
@RequestMapping("/inscricao")
public class InscricaoController {

    InscricaoRepository inscricaoRepository;
    EventoRepository eventoRepository;

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
        Usuario teste = new Usuario();
        teste.setId_usuario(id);
        ModelAndView mv = new ModelAndView("listarInscricoes");
        Iterable<Inscricao> inscricaos = inscricaoRepository.findInscricaoByIdUsuario(teste);
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

/*

    @GetMapping("{id}")
    public ResponseEntity<Inscricao> getById(@PathVariable("id") Integer id) {
        Optional<Inscricao> existingItemOptional = repository.findById(id);
        if (existingItemOptional.isPresent()) {
            return new ResponseEntity<>(existingItemOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
*/

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
