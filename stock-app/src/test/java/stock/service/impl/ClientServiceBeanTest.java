package stock.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import stock.model.Client;
import stock.repository.ClientDao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceBeanTest {

    @Mock
    private ClientDao clientDao;

    @InjectMocks
    private ClientServiceBean clientService;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client("Jan", "Kowalski", "jan.kowalski@example.com", new BigDecimal("10000.00"));
        testClient.setId(1L);
    }

    @Test
    void should_find_all_clients() {
        // given
        List<Client> clients = Arrays.asList(testClient);
        when(clientDao.findAll()).thenReturn(clients);

        // when
        List<Client> result = clientService.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testClient);
        verify(clientDao).findAll();
    }

    @Test
    void should_find_client_by_id() {
        // given
        when(clientDao.findById(1L)).thenReturn(Optional.of(testClient));

        // when
        Optional<Client> result = clientService.findById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testClient);
        verify(clientDao).findById(1L);
    }

    @Test
    void should_find_client_by_email() {
        // given
        when(clientDao.findByEmail("jan.kowalski@example.com")).thenReturn(Optional.of(testClient));

        // when
        Optional<Client> result = clientService.findByEmail("jan.kowalski@example.com");

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testClient);
        verify(clientDao).findByEmail("jan.kowalski@example.com");
    }

    @Test
    void should_create_new_client() {
        // given
        when(clientDao.findByEmail("anna.nowak@example.com")).thenReturn(Optional.empty());
        when(clientDao.save(any(Client.class))).thenAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            client.setId(2L);
            return client;
        });

        // when
        Client result = clientService.createClient("Anna", "Nowak", "anna.nowak@example.com", new BigDecimal("25000.00"));

        // then
        assertThat(result.getFirstName()).isEqualTo("Anna");
        assertThat(result.getLastName()).isEqualTo("Nowak");
        assertThat(result.getEmail()).isEqualTo("anna.nowak@example.com");
        assertThat(result.getBalance()).isEqualTo(new BigDecimal("25000.00"));
        verify(clientDao).findByEmail("anna.nowak@example.com");
        verify(clientDao).save(any(Client.class));
    }

    @Test
    void should_throw_exception_when_creating_client_with_existing_email() {
        // given
        when(clientDao.findByEmail("jan.kowalski@example.com")).thenReturn(Optional.of(testClient));

        // when & then
        assertThatThrownBy(() -> clientService.createClient("Jan", "Kowalski", "jan.kowalski@example.com", new BigDecimal("10000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client with email jan.kowalski@example.com already exists");

        verify(clientDao).findByEmail("jan.kowalski@example.com");
        verify(clientDao, never()).save(any(Client.class));
    }

    @Test
    void should_throw_exception_when_creating_client_with_negative_balance() {
        // given
        when(clientDao.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> clientService.createClient("Test", "User", "test@example.com", new BigDecimal("-100.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Balance cannot be negative");

        verify(clientDao).findByEmail("test@example.com");
        verify(clientDao, never()).save(any(Client.class));
    }

    @Test
    void should_update_client_balance() {
        // given
        when(clientDao.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientDao.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Client result = clientService.updateBalance(1L, new BigDecimal("15000.00"));

        // then
        assertThat(result.getBalance()).isEqualTo(new BigDecimal("15000.00"));
        verify(clientDao).findById(1L);
        verify(clientDao).save(testClient);
    }

    @Test
    void should_throw_exception_when_updating_balance_to_negative() {
        // given
        when(clientDao.findById(1L)).thenReturn(Optional.of(testClient));

        // when & then
        assertThatThrownBy(() -> clientService.updateBalance(1L, new BigDecimal("-1000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Balance cannot be negative");

        verify(clientDao).findById(1L);
        verify(clientDao, never()).save(any(Client.class));
    }

    @Test
    void should_throw_exception_when_updating_balance_of_non_existing_client() {
        // given
        when(clientDao.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> clientService.updateBalance(999L, new BigDecimal("1000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client not found with id: 999");

        verify(clientDao).findById(999L);
        verify(clientDao, never()).save(any(Client.class));
    }
}