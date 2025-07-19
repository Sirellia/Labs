package com.example.demo.service;

import com.example.demo.dto.VisitorRequestDTO;
import com.example.demo.model.Visitor;
import com.example.demo.repository.VisitorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {
    @Mock
    private VisitorJpaRepository visitorRepository;
    @InjectMocks
    private VisitorService visitorService;

    private Visitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new Visitor(1L, "Test", 20, Visitor.Gender.MALE);
    }

    @Test
    void saveVisitor() {
        when(visitorRepository.save(any(Visitor.class))).thenReturn(visitor);
        var dto = new VisitorRequestDTO("Test", 20, Visitor.Gender.MALE);
        var result = visitorService.save(dto);
        assertThat(result.id()).isEqualTo(1L);
        verify(visitorRepository).save(any(Visitor.class));
    }

    @Test
    void findAllVisitors() {
        when(visitorRepository.findAll()).thenReturn(List.of(visitor));
        var result = visitorService.findAll();
        assertThat(result).hasSize(1);
        verify(visitorRepository).findAll();
    }

    @Test
    void findVisitorById() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        var result = visitorService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1L);
    }

    @Test
    void updateVisitor() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(visitorRepository.save(any(Visitor.class))).thenReturn(visitor);
        var dto = new VisitorRequestDTO("Updated", 22, Visitor.Gender.FEMALE);
        var result = visitorService.update(1L, dto);
        assertThat(result.name()).isEqualTo("Updated");
        assertThat(result.age()).isEqualTo(22);
        assertThat(result.gender()).isEqualTo(Visitor.Gender.FEMALE);
    }

    @Test
    void updateVisitorNotFound() {
        when(visitorRepository.findById(2L)).thenReturn(Optional.empty());
        var dto = new VisitorRequestDTO("Updated", 22, Visitor.Gender.FEMALE);
        assertThrows(IllegalArgumentException.class, () -> visitorService.update(2L, dto));
    }

    @Test
    void removeVisitor() {
        doNothing().when(visitorRepository).deleteById(1L);
        visitorService.remove(1L);
        verify(visitorRepository).deleteById(1L);
    }
} 