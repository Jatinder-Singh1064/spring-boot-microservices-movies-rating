package com.learn.moviecatalogservice.services;

import com.learn.moviecatalogservice.models.CatalogItem;
import com.learn.moviecatalogservice.models.Movie;
import com.learn.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    //BulkHead Pattern - feature of Resilience4j Circuit-Breaker pattern
    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
                    threadPoolKey = "movieInfoPool",    //creating a separate ThreadPool with unique name
                    threadPoolProperties = {
                            @HystrixProperty(name = "coreSize", value = "20"),      //at most or max size of threads to process requests
                            @HystrixProperty(name = "maxQueueSize", value = "10")   //waiting queue after 20 threads are busy executing requests- after 10 request will fallback/error and call CircuitBreaker
                    })
    public CatalogItem getCatalogItem(Rating rating) {
        //For each movieId, call MovieInfoService and get details
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }
}
