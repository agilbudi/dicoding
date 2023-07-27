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

const getAllBooksHandler = (request, h) => {
   const { reading, finished, name } = request.query;
   const isEmpty = books.length < 1;
   let newBook = [];

   if (!isEmpty) {
      newBook = books.map( ({id, name, publisher}) => 
         Object.assign({}, {
            id: id,
            name: name,
            publisher: publisher
         })
      );

      if (reading !== undefined) {
         newBook =  books.filter((book) => book.reading == reading).map( ({id, name, publisher}) => 
         Object.assign({}, {
            id: id,
            name: name,
            publisher: publisher
         })
      );
      }
      if (finished !== undefined) {
         newBook = books.filter((book) => book.finished == finished).map( ({id, name, publisher}) => 
         Object.assign({}, {
            id: id,
            name: name,
            publisher: publisher
         })
      );
      }
      if (name !== undefined) {
         newBook = books.filter((book) => book.name.toLowerCase().includes(name.toLowerCase()) ).map( ({id, name, publisher}) => 
         Object.assign({}, {
            id: id,
            name: name,
            publisher: publisher
         })
      );
      }
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

const deleteBookByIdHandler = (request, h) =>{
   const { bookId } = request.params;
   const index = books.findIndex((book) => book.id === bookId);

   if (index !== -1) {
      books.splice(index,1);
      const response = h.response({
         status: 'success',
         message: 'Buku berhasil dihapus'
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      status: 'fail',
      message: 'Buku gagal dihapus. Id tidak ditemukan'
   });
   response.code(404);
   return response;
}

const getQueryHandler = (request, h) =>{
   const { reading, finished, name } = request.query;


   if (reading !== undefined) {
      
   }
}

export {
   addBooksHandler, getAllBooksHandler, 
   getBookByIdHandler, editBookByIdHandler,
   deleteBookByIdHandler
};