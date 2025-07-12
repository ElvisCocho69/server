package com.api.server.controller.order;

import java.util.Date;
// import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.order.OrderDTO;
import com.api.server.dto.order.OrderCancellationDTO;
import com.api.server.service.order.OrderService;
// import com.api.server.util.OrderPdfExporter;
// import com.api.server.util.OrderExcelExporter;
// import com.api.server.util.OrderCsvExporter;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> listAllOrders(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Date startDate,
        @RequestParam(required = false) Date endDate,
        Pageable pageable
    ) {
        try {
            Page<OrderDTO> orders = orderService.findAll(startDate, endDate, status, pageable);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // @GetMapping("/export/pdf")
    // public ResponseEntity<byte[]> exportToPDF(
    //     @RequestParam(required = false) String status,
    //     @RequestParam(required = false) Date startDate,
    //     @RequestParam(required = false) Date endDate
    // ) {
    //     try {
    //         Page<OrderDTO> ordersPage = orderService.findAll(startDate, endDate, status, Pageable.unpaged());
    //         List<OrderDTO> orders = ordersPage.getContent();

    //         OrderPdfExporter exporter = new OrderPdfExporter();
    //         byte[] pdfBytes = exporter.export(orders);

    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.APPLICATION_PDF);
    //         headers.setContentDispositionFormData("filename", "orders.pdf");

    //         return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }

    // @GetMapping("/export/excel")
    // public ResponseEntity<byte[]> exportToExcel(
    //     @RequestParam(required = false) String status,
    //     @RequestParam(required = false) Date startDate,
    //     @RequestParam(required = false) Date endDate
    // ) {
    //     try {
    //         Page<OrderDTO> ordersPage = orderService.findAll(startDate, endDate, status, Pageable.unpaged());
    //         List<OrderDTO> orders = ordersPage.getContent();

    //         OrderExcelExporter exporter = new OrderExcelExporter();
    //         byte[] excelBytes = exporter.export(orders);

    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    //         headers.setContentDispositionFormData("filename", "orders.xlsx");

    //         return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }

    // @GetMapping("/export/csv")
    // public ResponseEntity<byte[]> exportToCSV(
    //     @RequestParam(required = false) String status,
    //     @RequestParam(required = false) Date startDate,
    //     @RequestParam(required = false) Date endDate
    // ) {
    //     try {
    //         Page<OrderDTO> ordersPage = orderService.findAll(startDate, endDate, status, Pageable.unpaged());
    //         List<OrderDTO> orders = ordersPage.getContent();

    //         OrderCsvExporter exporter = new OrderCsvExporter();
    //         byte[] csvBytes = exporter.export(orders);

    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.parseMediaType("text/csv"));
    //         headers.setContentDispositionFormData("filename", "orders.csv");

    //         return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrderById(@PathVariable Long id) {
        Optional<OrderDTO> order = orderService.findOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @PostMapping
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO order = orderService.saveOrderWithOrderDetailsAndStructure(orderDTO);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        OrderDTO order = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(
        @PathVariable Long id,
        @RequestBody OrderCancellationDTO cancellationDTO
    ) {
        try {
            OrderDTO order = orderService.cancelOrder(id, cancellationDTO.getCancellationreason());
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/by-ordernumber/{ordernumber}")
    public ResponseEntity<OrderDTO> findOrderByOrdernumber(@PathVariable String ordernumber) {
        try {
            Optional<OrderDTO> order = orderService.findOrderByOrdernumber(ordernumber);
            if (order.isPresent()) {
                return ResponseEntity.ok(order.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}