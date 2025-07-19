package com.example.demo.controller;

import com.example.demo.dto.VisitorRequestDTO;
import com.example.demo.dto.VisitorResponseDTO;
import com.example.demo.model.Visitor;
import com.example.demo.service.VisitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitorController.class)
class VisitorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VisitorService visitorService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllVisitors() throws Exception {
        Mockito.when(visitorService.findAll()).thenReturn(List.of(new VisitorResponseDTO(1L, "Test", 20, Visitor.Gender.MALE)));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getVisitorById() throws Exception {
        Mockito.when(visitorService.findById(1L)).thenReturn(Optional.of(new VisitorResponseDTO(1L, "Test", 20, Visitor.Gender.MALE)));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createVisitor() throws Exception {
        VisitorRequestDTO dto = new VisitorRequestDTO("Test", 20, Visitor.Gender.MALE);
        VisitorResponseDTO response = new VisitorResponseDTO(1L, "Test", 20, Visitor.Gender.MALE);
        Mockito.when(visitorService.save(any())).thenReturn(response);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateVisitor() throws Exception {
        VisitorRequestDTO dto = new VisitorRequestDTO("Updated", 22, Visitor.Gender.FEMALE);
        VisitorResponseDTO response = new VisitorResponseDTO(1L, "Updated", 22, Visitor.Gender.FEMALE);
        Mockito.when(visitorService.update(eq(1L), any())).thenReturn(response);
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void deleteVisitor() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
} 