package com.example.transactionstarter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdditionalTransactionTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getNonExistentTransactionReturns404() throws Exception {
        String randomId = UUID.randomUUID().toString();
        mvc.perform(MockMvcRequestBuilders.get("/api/transactions/" + randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCurrencyIsUppercased() throws Exception {
        String payload = "{\"customerId\":\"cust-up\",\"amount\":5.5,\"currency\":\"usd\",\"type\":\"DEBIT\"}";
        MvcResult res = mvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = mapper.readTree(res.getResponse().getContentAsString());
        Assertions.assertEquals("USD", node.get("currency").asText());
    }

    @Test
    void createMissingTypeIsRejected() throws Exception {
        String payload = "{\"customerId\":\"c-no-type\",\"amount\":10,\"currency\":\"EUR\"}";
        mvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomerTransactionsEmptyList() throws Exception {
        // choose a customer id unlikely to exist
        String customer = "no-such-customer-xyz";
        MvcResult res = mvc.perform(MockMvcRequestBuilders.get("/api/customers/" + customer + "/transactions"))
                .andExpect(status().isOk())
                .andReturn();

        String body = res.getResponse().getContentAsString();
        // should be an empty JSON array
        Assertions.assertEquals("[]", body.trim());
    }
}
