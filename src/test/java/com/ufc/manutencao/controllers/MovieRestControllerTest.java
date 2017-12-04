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
import com.ufc.manutencao.domain.BorrowView;
import com.ufc.manutencao.domain.Movie;
import com.ufc.manutencao.domain.MovieView;
import com.ufc.manutencao.domain.User;
import com.ufc.manutencao.repositories.ActorRepository;
import com.ufc.manutencao.repositories.MovieRepository;
import com.ufc.manutencao.repositories.UserRepository;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MovieRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Movie> movieList = new ArrayList<>();

    private List<Actor> actorList = new ArrayList<>();

    private List<User> userList = new ArrayList<>();

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private UserRepository userRepository;

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
        actorList.add(actorRepository.save(new Actor(new HashSet<>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(2)))), "Ian McKellen")));
        actorList.add(actorRepository.save(new Actor(new HashSet<>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(2)))), "Orlando Bloom")));
        actorList.add(actorRepository.save(new Actor(new HashSet<>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(3)))), "Sean Bean")));
        actorList.add(actorRepository.save(new Actor(new HashSet<>(new ArrayList<>(Arrays.asList(movieList.get(0)))), "Tomasz Karolak")));
        actorList.add(actorRepository.save(new Actor(new HashSet<>(new ArrayList<>(Arrays.asList(movieList.get(0)))), "Agnieszka Dygant")));
        userList.add(userRepository.save(new User("First user")));
    }

    @Test
    public void movieNotFound() throws Exception {
        mockMvc.perform(get("/movies/999")
                .content(this.json(new Actor()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleMovie() throws Exception {
        mockMvc.perform(get("/movies/" + movieList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.title", is(movieList.get(0).title)));
    }

    @Test
    public void readMovies() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(movieList.get(0).title)))
                .andExpect(jsonPath("$[1].id", is(movieList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].title", is(movieList.get(1).title)))
                .andExpect(jsonPath("$[2].id", is(movieList.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].title", is(movieList.get(2).title)))
                .andExpect(jsonPath("$[3].id", is(movieList.get(3).getId().intValue())))
                .andExpect(jsonPath("$[3].title", is(movieList.get(3).title)));
    }

    @Test
    public void createMovie() throws Exception {
        String movieJson = json(new Movie("Title", "new"));
        this.mockMvc.perform(post("/movies")
                .contentType(contentType)
                .content(movieJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Test
    public void readMoviesByCategory() throws Exception {
        mockMvc.perform(get("/movies/category/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(movieList.get(0).title)))
                .andExpect(jsonPath("$[0].category", is("new")))
                .andExpect(jsonPath("$[1].id", is(movieList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].title", is(movieList.get(1).title)))
                .andExpect(jsonPath("$[1].category", is("new")));
    }

//    @Test
//    public void readAvailableMovies() throws Exception {
//        mockMvc.perform(get("/movies/return"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(contentType))
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].id", is(movieList.get(0).getId().intValue())))
//                .andExpect(jsonPath("$[0].title", is(movieList.get(0).title)))
//                .andExpect(jsonPath("$[0].available", is(new Boolean(true))))
//                .andExpect(jsonPath("$[1].id", is(movieList.get(1).getId().intValue())))
//                .andExpect(jsonPath("$[1].title", is(movieList.get(1).title)))
//                .andExpect(jsonPath("$[1].available", is(new Boolean(true))))
//                .andExpect(jsonPath("$[2].id", is(movieList.get(2).getId().intValue())))
//                .andExpect(jsonPath("$[2].title", is(movieList.get(2).title)))
//                .andExpect(jsonPath("$[2].available", is(new Boolean(true))))
//                .andExpect(jsonPath("$[3].id", is(movieList.get(3).getId().intValue())))
//                .andExpect(jsonPath("$[3].title", is(movieList.get(3).title)))
//                .andExpect(jsonPath("$[3].available", is(new Boolean(true))));
//    }

    @Test
    public void returnMoviesBorrowedByUser() throws Exception {
        //Borrow
        MovieView[] moviesToBorrow = new MovieView[]{
                new MovieView(movieList.get(0).getId(), movieList.get(0).getTitle()),
                new MovieView(movieList.get(1).getId(), movieList.get(1).getTitle()),
                new MovieView(movieList.get(3).getId(), movieList.get(3).getTitle())
        };
        String borrowViewJson = json(new BorrowView(userList.get(0).getId(), new Double(0), Arrays.asList(moviesToBorrow)));
        this.mockMvc.perform(post("/movies/borrow")
                .contentType(contentType)
                .content(borrowViewJson));
        //Read returned movie
//        this.mockMvc.perform(get("/movies/userId/" + userList.get(0).getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(contentType))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].id", is(movieList.get(0).getId().intValue())))
//                .andExpect(jsonPath("$[0].title", is(movieList.get(0).title)))
//                .andExpect(jsonPath("$[0].available", is(new Boolean(false))))
//                .andExpect(jsonPath("$[1].id", is(movieList.get(1).getId().intValue())))
//                .andExpect(jsonPath("$[1].title", is(movieList.get(1).title)))
//                .andExpect(jsonPath("$[1].available", is(new Boolean(false))))
//                .andExpect(jsonPath("$[2].id", is(movieList.get(3).getId().intValue())))
//                .andExpect(jsonPath("$[2].title", is(movieList.get(3).title)))
//                .andExpect(jsonPath("$[2].available", is(new Boolean(false))));
    }

    @Test
    public void borrowMovie() throws Exception {
        MovieView[] moviesToBorrow = new MovieView[]{
                new MovieView(movieList.get(0).getId(), movieList.get(0).getTitle()),
                new MovieView(movieList.get(1).getId(), movieList.get(1).getTitle()),
                new MovieView(movieList.get(2).getId(), movieList.get(2).getTitle()),
                new MovieView(movieList.get(3).getId(), movieList.get(3).getTitle())
        };
        String borrowViewJson = json(new BorrowView(userList.get(0).getId(), new Double(0), Arrays.asList(moviesToBorrow)));
//        this.mockMvc.perform(post("/movies/borrow")
//                .contentType(contentType)
//                .content(borrowViewJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userId", is(userList.get(0).getId().intValue())))
//                .andExpect(jsonPath("$.cost", is(new Double(41.25))))
//                .andExpect(jsonPath("$.movies", hasSize(4)))
//                .andExpect(jsonPath("$.movies[0].id", is(movieList.get(0).getId().intValue())))
//                .andExpect(jsonPath("$.movies[0].title", is(movieList.get(0).title)))
//                .andExpect(jsonPath("$.movies[1].id", is(movieList.get(1).getId().intValue())))
//                .andExpect(jsonPath("$.movies[1].title", is(movieList.get(1).title)))
//                .andExpect(jsonPath("$.movies[2].id", is(movieList.get(2).getId().intValue())))
//                .andExpect(jsonPath("$.movies[2].title", is(movieList.get(2).title)))
//                .andExpect(jsonPath("$.movies[3].id", is(movieList.get(3).getId().intValue())))
//                .andExpect(jsonPath("$.movies[3].title", is(movieList.get(3).title)));
    }

    @Test
    public void returnMovie() throws Exception {
        //Borrow
        MovieView[] moviesToBorrow = new MovieView[]{
                new MovieView(movieList.get(0).getId(), movieList.get(0).getTitle()),
                new MovieView(movieList.get(1).getId(), movieList.get(1).getTitle()),
                new MovieView(movieList.get(2).getId(), movieList.get(2).getTitle()),
                new MovieView(movieList.get(3).getId(), movieList.get(3).getTitle())
        };
        String borrowViewJson = json(new BorrowView(userList.get(0).getId(), new Double(0), Arrays.asList(moviesToBorrow)));
        this.mockMvc.perform(post("/movies/borrow")
                .contentType(contentType)
                .content(borrowViewJson));
        //Return
        MovieView[] moviesToReturn = new MovieView[]{
                new MovieView(movieList.get(2).getId(), movieList.get(2).getTitle()),
        };
        String borrowViewJson2 = json(new BorrowView(userList.get(0).getId(), new Double(0), Arrays.asList(moviesToReturn)));
        this.mockMvc.perform(post("/movies/return")
                .contentType(contentType)
                .content(borrowViewJson2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(movieList.get(2).getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(movieList.get(2).title)))
                .andExpect(jsonPath("$[0].available", is(new Boolean(true))));
        //Read returned movie
        this.mockMvc.perform(get("/movies/" + movieList.get(2).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.available", is(new Boolean(true))));
    }
}
