package com.ontariotechu.sofe3980U.binarycalculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebMvcTest(BinaryController.class)
public class BinaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getDefault() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("operand1", "operand2", "operand1Focused"))
                .andExpect(model().attribute("operand1Focused", false));
    }

    @Test
    public void getParameter() throws Exception {
        mockMvc.perform(get("/").param("operand1", "111"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attribute("operand1", "111"))
                .andExpect(model().attribute("operand1Focused", true));
    }

    @Test
    public void postParameter() throws Exception {
        mockMvc.perform(post("/")
                .param("operand1", "101")
                .param("operator", "+")
                .param("operand2", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attribute("operand1", "101"));
    }

    // Insert the additional test cases here

    // Test Case 1: Error Handling for Non-Binary Input
    @Test
    public void testErrorHandlingForNonBinaryInput() throws Exception {
        mockMvc.perform(post("/")
                .param("operand1", "102")
                .param("operator", "+")
                .param("operand2", "110"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("calculator"));
    }

    // Test Case 2: Large Number Calculation
    @Test
    public void testLargeNumberCalculation() throws Exception {
        mockMvc.perform(post("/")
                .param("operand1", "1111111111111111")
                .param("operator", "+")
                .param("operand2", "1111111111111111"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("result", "11111111111111110"))
                .andExpect(view().name("result"));
    }

    // Test Case 3: Edge Case with Single Operand
    @Test
    public void testEdgeCaseWithSingleOperand() throws Exception {
        mockMvc.perform(post("/")
                .param("operand1", "101010")
                .param("operator", "+"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("calculator"));
    }
}
