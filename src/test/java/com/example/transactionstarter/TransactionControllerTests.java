package com.example.transactionstarter;

import com.example.transactionstarter.transaction.TransactionStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createAndGetTransaction() throws Exception {
        String payload = "{\"customerId\":\"cust-1\",\"amount\":100.50,\"currency\":\"usd\",\"type\":\"DEBIT\"}";
        MvcResult res = mvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = mapper.readTree(res.getResponse().getContentAsString());
        Assertions.assertNotNull(node.get("id"));

        String id = node.get("id").asText();

        mvc.perform(MockMvcRequestBuilders.get("/api/transactions/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void updateStatusTransitions() throws Exception {
        String payload = "{\"customerId\":\"cust-2\",\"amount\":50,\"currency\":\"EUR\",\"type\":\"CREDIT\"}";
        MvcResult res = mvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = mapper.readTree(res.getResponse().getContentAsString());
        String id = node.get("id").asText();

        // valid transition PENDING -> COMPLETED
        String upd = "{\"status\":\"COMPLETED\"}";
        mvc.perform(MockMvcRequestBuilders.patch("/api/transactions/" + id + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(upd))
                .andExpect(status().isOk());

        // second attempt should fail (can't change from COMPLETED)
        mvc.perform(MockMvcRequestBuilders.patch("/api/transactions/" + id + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"FAILED\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomerTransactions() throws Exception {
        String payload1 = "{\"customerId\":\"cust-3\",\"amount\":10,\"currency\":\"GBP\",\"type\":\"DEBIT\"}";
        String payload2 = "{\"customerId\":\"cust-3\",\"amount\":20,\"currency\":\"GBP\",\"type\":\"CREDIT\"}";

        mvc.perform(MockMvcRequestBuilders.post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(payload1)).andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(payload2)).andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/api/customers/cust-3/transactions")).andExpect(status().isOk());
    }

    @Test
    void validationRejectsBadAmountAndCurrency() throws Exception {
        String bad = "{\"customerId\":\"c4\",\"amount\":0,\"currency\":\"US\",\"type\":\"DEBIT\"}";
        mvc.perform(MockMvcRequestBuilders.post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(bad))
                .andExpect(status().isBadRequest());
    }
}
