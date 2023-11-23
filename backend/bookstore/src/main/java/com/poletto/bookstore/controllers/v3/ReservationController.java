package com.poletto.bookstore.controllers.v3;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poletto.bookstore.dto.v2.ReservationDTOv2;
import com.poletto.bookstore.exceptions.exceptionresponse.ExceptionResponse;
import com.poletto.bookstore.services.v3.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController("ReservationControllerV3")
@RequestMapping(value = "/reservations/v3")
@Tag(name = "Reservation Controller V3", description = "Endpoints related to reservation resources management")
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping
	@Operation(
		summary = "Returns a page of reservations",
		description = "Get reservations page filtered by the parameters provided",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<Page<ReservationDTOv2>> findAllPaged(
			@Parameter(description = "Page index") @RequestParam(value = "page", defaultValue = "0") Integer page,
			@Parameter(description = "Page size") @RequestParam(value = "size", defaultValue = "5") Integer size,
			@Parameter(description = "Page sorting option") @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@Parameter(description = "Page sorting direction") @RequestParam(value = "sort", defaultValue = "asc") String sort,
			@Parameter(description = "Reservation starting date") @RequestParam(value = "startingDate", defaultValue = "") LocalDate startingDate,
			@Parameter(description = "Reservation devolution date") @RequestParam(value = "devolutionDate", defaultValue = "") LocalDate devolutionDate,
			@Parameter(description = "Reservation client ID") @RequestParam(value = "client", defaultValue = "") Long clientId,
			@Parameter(description = "Reservation book ID") @RequestParam(value = "book", defaultValue = "") Long bookId,
			@Parameter(description = "Reservation status") @RequestParam(value = "status", defaultValue = "all") String status
		) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.valueOf(sort.toUpperCase()), orderBy));
		
		Page<ReservationDTOv2> reservations = reservationService.findAllPaged(pageable, startingDate, devolutionDate, clientId, bookId, status);
		
		return ResponseEntity.ok().body(reservations);
		
	}

	@GetMapping(value = "/{id}")
	@Operation(
		summary = "Returns a reservation by ID",
		description = "Get reservation details based on the ID provided",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<ReservationDTOv2> findById(
			@Parameter(description = "Category ID")
			@PathVariable Long id
		) {
		ReservationDTOv2 reservation = reservationService.findById(id);
		return ResponseEntity.ok().body(reservation);
	}

	@PostMapping
	@Operation(
		summary = "Creates a new reservation",
		description = "Creates a new reservation with provided data and then return new reservation details",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(
				description = "Created",
				responseCode = "201",
				useReturnTypeSchema = true
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<ReservationDTOv2> insert(
			@Parameter(
					description = "Payload with new reservation data",
					content = @Content(schema = @Schema(implementation = ReservationDTOv2.class))
				)
			@RequestBody ReservationDTOv2 dto
		) {
		dto = reservationService.reserveBooks(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/return/{id}")
	@Operation(
		summary = "Deletes a reservation by ID",
		description = "Deletes reservation based on the ID provided",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "204"
			),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
			@ApiResponse(description = "Internal error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
		}
	)
	public ResponseEntity<ReservationDTOv2> returnReservation(
			@Parameter(description = "Reservation ID")
			@PathVariable Long id
		) {
		reservationService.returnReservation(id);
		return ResponseEntity.noContent().build();
	}

}
