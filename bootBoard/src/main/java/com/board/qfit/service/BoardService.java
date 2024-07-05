package com.board.qfit.service;

import org.springframework.stereotype.Service;

import com.board.qfit.dao.BoardDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardDAO boardDAO;
}
