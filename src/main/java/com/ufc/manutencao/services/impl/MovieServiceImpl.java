package com.ufc.manutencao.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufc.manutencao.domain.BorrowView;
import com.ufc.manutencao.domain.Movie;
import com.ufc.manutencao.domain.MovieView;
import com.ufc.manutencao.exceptions.MovieCategoryDoesntExist;
import com.ufc.manutencao.exceptions.MovieCountLimitException;
import com.ufc.manutencao.exceptions.MovieNotFoundException;
import com.ufc.manutencao.exceptions.UserNotFoundException;
import com.ufc.manutencao.repositories.MovieRepository;
import com.ufc.manutencao.repositories.UserRepository;
import com.ufc.manutencao.services.MovieService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Movie> readAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie readOne(Long id) {
        validateMovie(id);
        return movieRepository.findOne(id);
    }

    @Override
    public Movie save(Movie movie) {
        validateMovieCategory(movie);
        return movieRepository.save(new Movie(movie.title, movie.getCategory()));
    }

    @Override
    public Movie update(Movie movie, Long id) {
        if (!movieRepository.findById(id).isPresent()) {
            save(movie);
        }
        return movieRepository.save(new Movie(movie.title, movie.getCategory(), id));
    }

    @Override
    public void delete(Long id) {
        validateMovie(id);
        movieRepository.findById(id).get().getActors().forEach(
                actor -> actor.unmapMovie(id)
        );
        movieRepository.delete(id);
    }

    @Override
    public BorrowView borrow(BorrowView borrowView) {
        Movie movie;
        validateUser(borrowView.getUserId());
        Collection<MovieView> borrowedMovieView = new ArrayList<>();
        Collection<Movie> borrowedMovie = new ArrayList<>();
        validateMovieLimit(borrowView.getUserId(), borrowView.getMovies().size());
        for (MovieView movieItem : borrowView.getMovies()) {
            validateMovie(movieItem.getId());
            movie = movieRepository.findById(movieItem.getId()).get();
            borrowedMovie.add(movie);
            if (movie.isAvailable()) {
                movie.setBorrower(userRepository.findById(borrowView.getUserId()).get());
                movieRepository.save(movie);
                borrowedMovieView.add(new MovieView(movie.getId(), movie.getTitle()));
            }
        }
        /*Count cost*/
        List<String> categories = new ArrayList<>();
        borrowedMovie.forEach(
                movieItem -> {
                    categories.add(movieItem.getCategory());
                });
        Integer newCount, bestCount, othersCount;
        BigDecimal cost;
        newCount = Collections.frequency(categories, "new");
        bestCount = Collections.frequency(categories, "best");
        othersCount = Collections.frequency(categories, "others");
        cost = new BigDecimal(20 * newCount + 15 * bestCount + 10 * othersCount);
        if (categories.size() == 4 && othersCount >= 1) {
            cost.subtract(new BigDecimal(10));
        }
        if (newCount >= 2) {
            cost.multiply(new BigDecimal(0.75));
        }
        return new BorrowView(borrowView.getUserId(), cost.doubleValue(), borrowedMovieView);
    }

    @Override
    public List<Movie> returnMovies(BorrowView borrowView) {
        List<Movie> moviesToReturn = new ArrayList<>();
        List<Long> usersMoviesIds = new ArrayList<>(), requestMoviesIds = new ArrayList<>();
        validateUser(borrowView.getUserId());
        userRepository.findById(borrowView.getUserId()).get().getBorrowedMovies().forEach(
                movie -> usersMoviesIds.add(movie.getId())
        );
        borrowView.getMovies().forEach(movie -> requestMoviesIds.add(movie.getId()));
        usersMoviesIds.retainAll(requestMoviesIds);
        usersMoviesIds.forEach(
                movieId -> {
                    validateMovie(movieId);
                    moviesToReturn.add(movieRepository.findById(movieId).get());
                    movieRepository.save(movieRepository.findById(movieId).get().setBorrower(null));

                }
        );
        return moviesToReturn;
    }

    @Override
    public List<Movie> readAvailable() {
        return movieRepository
                .findAll()
                .stream()
                .filter(movie -> movie.isAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> readByCategory(String category) {
        return movieRepository
                .findAll()
                .stream()
                .filter(movie -> movie.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> readByUser(Long id) {
        List<Movie> returnMovies = new ArrayList<>();
        for (Movie movie : movieRepository.findAll()) {
            if (movie.getBorrower() != null) {
                if (movie.getBorrower().getId().equals(id)) {
                    returnMovies.add(movie);
                }
            }
        }
        return returnMovies;
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));

    }

    private void validateMovieLimit(Long userId, int moviesCount) {
        userRepository.findById(userId)
                .ifPresent(user -> {
                    if (user.getBorrowedMovies().size() + moviesCount > 10) {
                        throw new MovieCountLimitException(userId);
                    }
                });
    }

    private void validateMovie(Long movieId) {
        movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException(movieId));
    }

    private void validateMovieCategory(Movie movie) {
        String[] categories = new String[]{"new", "best", "others"};
        if (!Arrays.asList(categories).contains(movie.getCategory())) {
            throw new MovieCategoryDoesntExist(movie.getCategory());
        }
    }
}
