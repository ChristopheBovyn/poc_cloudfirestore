package com.example.cloudfirestore.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CountryRequest {
    private String documentName;
    private HashMap<String,Object> data;
}
