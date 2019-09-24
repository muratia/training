package com.am.training.demo.controller;

import com.am.training.demo.entity.Person;
import com.am.training.demo.exception.NoPersonsException;
import com.am.training.demo.exception.PersonNotFoundException;
import com.am.training.demo.processor.CvsProcessor;
import com.am.training.demo.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("persons")
class PersonController {

    private static final Logger logger = LoggerFactory.getLogger (PersonController.class);

    @Autowired
    private PersonService personService;

    @SuppressWarnings("SpellCheckingInspection")
    @PostMapping(value = "", produces = "application/json")
    public List<Person> initializDb() throws IOException {
        CvsProcessor cvsProcessor = new CvsProcessor ();
        final String fileName = "data.csv";

        File file = new ClassPathResource (fileName).getFile ();

        List<Person> persons = CvsProcessor.read (file.getPath ());
        return personService.save (persons);
    }

    @GetMapping(value = "", produces = "application/json")
    public List<Person> getPersons() throws NoPersonsException {

        return personService.findPersons ();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public Person searchById(@PathVariable Long id) throws PersonNotFoundException {
        return personService.find (id);
    }

    @GetMapping("color/{color}")
    public List<Person> searchByColor(@PathVariable String color) throws NoPersonsException {
        return personService.findByColorName (color);
    }
}
