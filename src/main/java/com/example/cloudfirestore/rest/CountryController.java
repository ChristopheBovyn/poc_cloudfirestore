package com.example.cloudfirestore.rest;

import com.example.cloudfirestore.exceptions.DocumentAlreadyExistException;
import com.example.cloudfirestore.exceptions.UnknownDocumentException;
import com.example.cloudfirestore.request.CountryRequest;
import com.example.cloudfirestore.services.CountryService;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.List;


@RestController
@NoArgsConstructor
@RequestMapping("country")
public class CountryController {

    private CountryService service = new CountryService();
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addCountry(@RequestBody CountryRequest request) {
        try{
            this.service.addCountry(request.getDocumentName(),request.getData());
        }catch(DocumentAlreadyExistException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Document already exist");
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<HashMap<String,Object>> getCountries(){
        try{
            var countries = this.service.getCountries();
            return countries;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public void modifyCountry(@RequestBody CountryRequest request) {
        try {
            this.service.updateCoutry(request.getDocumentName(),request.getData());
        } catch (UnknownDocumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Document does not exist");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
