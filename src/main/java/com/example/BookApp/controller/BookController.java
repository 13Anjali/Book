package com.example.BookApp.controller;

import com.example.BookApp.model.Book;
import com.example.BookApp.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/Book")
    public ResponseEntity<List<Book>> getAllBooks(){
          try{
              List<Book> bookList = new ArrayList<>(bookRepo.findAll());

              if(bookList.isEmpty()){
                  return new ResponseEntity<>(HttpStatus.NO_CONTENT);
              }
              return new ResponseEntity<>(bookList,HttpStatus.OK);
          }
          catch (Exception exception){
              return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Book/{id}")
    public ResponseEntity<Book> getBooksById(@PathVariable Long id){
        Optional<Book> bookOptional=bookRepo.findById(id);
        return bookOptional.map(book -> new ResponseEntity<>(book, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Book")
    public ResponseEntity<Book> addBooks(@RequestBody Book book){
        try{
            Book bookObj=bookRepo.save(book);
            return new ResponseEntity<>(bookObj,HttpStatus.CREATED);
        } catch(Exception exception){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/Book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id,@RequestBody Book book){
          try{
              Optional<Book>bookData=bookRepo.findById(id);
              if(bookData.isPresent()){
                  Book updatedBookData=bookData.get();
                  updatedBookData.setTitle(book.getTitle());
                  updatedBookData.setAuthor(book.getAuthor());

                  Book bookObj=bookRepo.save(updatedBookData);
                  return new ResponseEntity<>(bookObj,HttpStatus.CREATED);
              }
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          catch (Exception exception){
              return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
          }
    }

    @DeleteMapping("/Book/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id){
           try{
               Optional<Book>bookData=bookRepo.findById(id);
               if(bookData.isPresent()){
                   bookRepo.deleteById(id);
                   return new ResponseEntity<>(HttpStatus.OK);
               }
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
           catch(Exception exception){
               return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
           }
    }
}
