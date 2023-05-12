package com.libaitu.libaitu.service;

import com.libaitu.libaitu.dto.GetBooksPaginationRes;
import com.libaitu.libaitu.entity.Books;
import com.libaitu.libaitu.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ModelMapper modelMapper;

    public GetBooksPaginationRes getBooks (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> books = bookRepository.findAll(pageable);
        GetBooksPaginationRes res = modelMapper.map(books,GetBooksPaginationRes.class);
        return res;

    }





}
