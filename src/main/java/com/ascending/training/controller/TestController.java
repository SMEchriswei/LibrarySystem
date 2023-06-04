package com.ascending.training.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = {"/test"})
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/path/{pathValue}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getPath(@PathVariable String pathValue) {
        logger.info(String.format(">>>>>>> Path Value : %s", pathValue));
        return pathValue;
    }

    @RequestMapping(value = "/param", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getParam(@RequestParam(name = "param1") String param2) {
        logger.info(String.format(">>>>>>> Param : %s", param2));
        return param2;
    }

    @RequestMapping(value = "/all-param", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, String> getAllParam(@RequestParam Map<String, String> allParams) {
        logger.info(String.format(">>>>>>> Param : %s", allParams.entrySet()));
        return allParams;
    }

    @RequestMapping(value = "/header", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getHeader(@RequestHeader String token) {
        logger.info(String.format(">>>>>>> Token : %s", token));
        return token;
    }

    @RequestMapping(value = "/all-header", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, String> getAllHeader(@RequestHeader Map<String, String> headers) {
        logger.info(String.format(">>>>>>> Token : %s", headers.entrySet()));
        return headers;
    }

    @RequestMapping(value = "/body", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getBody(@RequestBody String body) {
        logger.info(String.format(">>>>>>> Body : %s", body));
        return body;
    }
}
