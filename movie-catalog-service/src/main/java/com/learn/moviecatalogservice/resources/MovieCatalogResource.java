package com.learn.moviecatalogservice.resources;

import com.learn.moviecatalogservice.models.CatalogItem;
import com.learn.moviecatalogservice.models.UserRating;
import com.learn.moviecatalogservice.services.MovieInfo;
import com.learn.moviecatalogservice.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

//    @Autowired
//    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        //For user Id, call RatingsDataService and get the rating details based on user's movies list
        UserRating userRating = userRatingInfo.getUserRating(userId);

        return userRating.getUserRatings().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());
    }
}

//Using WebClient.Builder = Spring Reactive
                   /*
                        Movie movie = webClientBuilder.build()
                                       .get()
                                       .uri("http://localhost:8082/movies/" + rating.getMovieId())
                                       .retrieve()
                                       .bodyToMono(Movie.class)
                                       .block();
                   **/