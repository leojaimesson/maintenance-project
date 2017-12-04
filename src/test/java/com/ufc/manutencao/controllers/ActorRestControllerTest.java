package com.ufc.manutencao.controllers;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.ufc.manutencao.domain.Actor;
import com.ufc.manutencao.domain.Movie;
import com.ufc.manutencao.repositories.ActorRepository;
import com.ufc.manutencao.repositories.MovieRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ActorRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Movie> movieList = new ArrayList<>();

    private List<Actor> actorList = new ArrayList<>();

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        movieRepository.deleteAllInBatch();
        actorRepository.deleteAllInBatch();

        movieList.add(movieRepository.save(new Movie("Listy do M.", "new")));
        movieList.add(movieRepository.save(new Movie("Lord of the rings", "new")));
        movieList.add(movieRepository.save(new Movie("Hobbit", "best")));
        movieList.add(movieRepository.save((new Movie("Game of thrones", "others"))));

        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(2)))), "Ian McKellen")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(2)))), "Orlando Bloom")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(3)))), "Sean Bean")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(0)))), "Tomasz Karolak")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(0)))), "Agnieszka Dygant")));
    }

    @Test
    public void actorNotFound() throws Exception {
        mockMvc.perform(get("/actors/999")
                .content(this.json(new Movie()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleActor() throws Exception {
        mockMvc.perform(get("/actors/" + actorList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(actorList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.name", is(actorList.get(0).getName())));
    }

    @Test
    public void readActors() throws Exception {
        mockMvc.perform(get("/actors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(actorList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(actorList.get(0).getName())))
                .andExpect(jsonPath("$[1].id", is(actorList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(actorList.get(1).getName())))
                .andExpect(jsonPath("$[2].id", is(actorList.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].name", is(actorList.get(2).getName())))
                .andExpect(jsonPath("$[3].id", is(actorList.get(3).getId().intValue())))
                .andExpect(jsonPath("$[3].name", is(actorList.get(3).getName())))
                .andExpect(jsonPath("$[4].id", is(actorList.get(4).getId().intValue())))
                .andExpect(jsonPath("$[4].name", is(actorList.get(4).getName())));
    }

    @Test
    public void createActor() throws Exception {
        String actorJson = json(new Actor(
                new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(2)))),
                "John Smith"
        ));

        this.mockMvc.perform(post("/actors")
                .contentType(contentType)
                .content(actorJson))
                .andExpect(status().isCreated());
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
