package com.example.takehome.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {

	public static final int ANONYMOUS_RATE_LIMIT_PER_SECOND = 5;
	public static final int AUTHENTICATED_RATE_LIMIT_PER_SECOND = 20;

	// thanks https://www.section.io/engineering-education/implement-rate-limiting-in-spring-boot/#The-token-bucket-algorithm
	private final Bucket anonymousBucket;
	private final Bucket authenticatedBucket;

	public BaseController() {
		super();

		// setupRateLimit() // (can't move to named method because we want FINAL Buckets)
		Bandwidth anonLimit = Bandwidth.classic(
				ANONYMOUS_RATE_LIMIT_PER_SECOND,
				Refill.greedy(ANONYMOUS_RATE_LIMIT_PER_SECOND, Duration.ofSeconds(1)));
		this.anonymousBucket = Bucket4j.builder().addLimit(anonLimit).build();

		Bandwidth authenticatedLimit = Bandwidth.classic(
				AUTHENTICATED_RATE_LIMIT_PER_SECOND,
				Refill.greedy(AUTHENTICATED_RATE_LIMIT_PER_SECOND, Duration.ofSeconds(1)));
		this.authenticatedBucket = Bucket4j.builder().addLimit(authenticatedLimit).build();
		log.trace("setup rateLimit() buckets");
	}

	void rateLimit() {
		log.trace("rateLimit()");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
				log.trace("request from ROLE_USER");
				if (!authenticatedBucket.tryConsume(1)) {
					log.info("request from ROLE_USER exceeded rate limit");
					throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
				}
			} else {
				log.trace("requst from authentication ROLE_ANONYMOUS");
				if (!anonymousBucket.tryConsume(1)) {
					log.info("request from authenticate ROLE_ANONYMOUS exceeded rate limit");
					throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
				}
			}
		} else {
			log.debug("request from UNKNOWN");
			if (!anonymousBucket.tryConsume(1)) {
				log.info("request from UNKNOWN exceeded rate limit");
				throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
			}
		}
	}
}