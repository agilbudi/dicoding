import { nanoid } from "nanoid";
import { books } from "./model.js";
import { errorDataHandler } from "./errorHandler.js";

const addBooksHandler = (request, h) => {
   const { name, year, author, summary, publisher, pageCount, readPage, reading } = request.payload;
   const id = nanoid(12);
   const insertedAt = new Date().toISOString();
   const updatedAt = insertedAt;
   const errorStatus= errorDataHandler('menambahkan',name, year, author, summary, publisher, pageCount, readPage, reading);

   if (!errorStatus.status) {
      const finished = (pageCount === readPage);
      const newBook = { 
            id, name, year, author, 
            summary, publisher, pageCount, readPage, 
            finished, reading, insertedAt, updatedAt
      };
      books.push(newBook);
      const isSuccess = books.filter((book) => book.id === id).length > 0;
      if (isSuccess) {
         const response = h.response({
            status: 'success',
            message: 'Buku berhasil ditambahkan',
            data:{
               bookId: id
            }
         });
         response.code(201);
         return response
      } else {
         const response = h.response({
            status: 'fail',
            message: 'Book gagal ditambahkan',
         });
         response.code(500);
         return response;
      }
   }
      
   const response = h.response({
      status: 'fail',
      message: errorStatus.message,
   });
   response.code(400);

   return response;
};

const getAllBooksHandler = () => {
   const isEmpty = books.length < 1;
   const newBook = [];
   if (!isEmpty) {
      books.map( ({id, name, publisher}) => 
         newBook.push({
            id: id,
            name: name,
            publisher: publisher
         })
      );
   }

   return {
      status: 'success',
      data: {
         books: newBook
      }
   }
};
const getBookByIdHandler = (request, h) =>{
   const { bookId } = request.params;
   const book = books.filter((book) => book.id === bookId)[0];

   if (book !== undefined) {
      return {
         status: 'success',
         data: {
            book
         }
      };
   }

   const response = h.response({
      status: 'fail',
      message: 'Buku tidak ditemukan',
   });
   response.code(404);
   return response;
}

const editBookByIdHandler = (request, h) =>{
   const { bookId } = request.params;
   const { name, year, author, summary, publisher, pageCount, readPage, reading } = request.payload;
   const updatedAt = new Date().toISOString();
   const index = books.findIndex((book) => book.id === bookId);
   
   if (index !== -1) {
      const errorStatus = errorDataHandler('memperbarui', name, year, author, summary, publisher, pageCount, readPage, reading);
      
      if (!errorStatus.status) {
         const finished = (pageCount === readPage);

         books.map((book, i) =>{
            if (i === index) {
               const id = book.id;
               const insertedAt = book.insertedAt;

               books[i] = {
                  id, name, year, author, 
                  summary, publisher, pageCount, readPage, 
                  finished, reading, insertedAt, updatedAt
               };
            }
         });

         const response = h.response({
            status: 'success',
            message: 'Buku berhasil diperbarui'
         });

         response.code(200);
         return response;
      }else{
         const response = h.response({
            status: 'fail',
            message: errorStatus.message,
         });

         response.code(400);
         return response;
      }
   };

   const response = h.response({
      status: 'fail',
      message: 'Gagal memperbarui buku. Id tidak ditemukan'
   });

   response.code(404);
   return response;
};










// const errorDataHandler = (name='', year=null, author='', summary='', publisher='', pageCount=null, readPage=null, reading=null) =>{
//    let statusError = true;
//    let onError = '';
    
//    if (name === '') {
//       onError = 'nama';
//    } else if (year === null) {
//       onError = 'tahun';
//    } else if (author === ''){
//       onError = 'penulis';
//    } else if (summary === '') {
//       onError = 'ringkasan';
//    } else if (publisher === '') {
//       onError = 'penerbit';
//    } else if (pageCount === null) {
//       onError = 'jumlah halaman';
//    } else if (readPage === null) {
//       onError = 'halaman dibaca';
//    } else if (reading === null) {
//       onError = 'reading';
//    } else{
//       statusError = !statusError;
//    }
//    if (!statusError) {
//       if (pageCount < readPage) {
//          onError = 'readPage tidak boleh lebih besar dari pageCount';
//          statusError = !statusError;
//       }
//    } else {
//       onError = `Mohon isi ${onError} buku`;
//    }

//    const errorMessage = statusError? `Gagal menambahkan buku. ${onError}`: null;
//    return{ statusError, errorMessage };
// };

export {addBooksHandler, getAllBooksHandler, getBookByIdHandler, editBookByIdHandler};