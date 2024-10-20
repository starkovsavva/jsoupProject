package com.tictactoe.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
public class Tormac {

    public List<Category> categories = new ArrayList<>();

}
