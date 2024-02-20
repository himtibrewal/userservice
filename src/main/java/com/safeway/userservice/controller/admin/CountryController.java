package com.safeway.userservice.controller.admin;

import com.safeway.userservice.controller.BaseController;
import com.safeway.userservice.dto.response.PaginationResponse;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.admin.Country;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.CountryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.safeway.userservice.utils.Commons.*;

@RestController
@RequestMapping("/admin")
public class CountryController extends BaseController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/country")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveCountry(@Valid @RequestBody Country country) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        country.setCreatedBy(userId);
        country.setUpdatedBy(userId);
        country.setCreatedOn(LocalDateTime.now());
        country.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Country>(countryService.saveCountry(country),
                "SF-200",
                "Country Created Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/country")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllCountry(
            @RequestParam(name = "paginated", defaultValue = PAGINATED_DEFAULT) boolean paginated,
            @RequestParam(name = "page", defaultValue = PAGE_O) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size,
            @RequestParam(name = "sort_by", defaultValue = SORT_BY_ID) String sortBy) {
        if (paginated) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<Country> countries = countryService.getAllCountryPageable(pageable);
            PaginationResponse<List<Country>> response = PaginationResponse.<List<Country>>builder()
                    .data(countries.getContent())
                    .totalPages(countries.getTotalPages())
                    .currentPage(countries.getNumber())
                    .totalItems(countries.getTotalElements())
                    .responseCode("SF-200")
                    .responseMessage("Country Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            List<Country> countries = countryService.getAllCountry();
            Response<List<Country>> response = Response.<List<Country>>builder()
                    .data(countries)
                    .responseCode("SF-200")
                    .responseMessage("Country Found Successfully")
                    .responseStatus(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping("/country/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCountryById(@PathVariable Long id) {
        Country country = countryService.getCountryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Country>(country,
                "SF-200",
                "Country Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/country/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCountry(@PathVariable("id") Long id, @Valid @RequestBody Country country) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        country.setUpdatedBy(userId);
        country.setUpdatedOn(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<Country>(countryService.updateCountry(id, country),
                "SF-200",
                "Country Updated Successfully",
                HttpStatus.OK.value()));
    }

    @DeleteMapping("/country/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null,
                "SF-204",
                "Country Deleted Successfully",
                HttpStatus.OK.value()));
    }

}
