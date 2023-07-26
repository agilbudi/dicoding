import { nanoid } from "nanoid";
import { books } from "./model.js";

const addBooksHandler = (request, h) => {
   const { name, year, author, summary, publisher, pageCount, readPage, reading } = request.payload;
   const id = nanoid(12);
   const insertedAt = new Date().toISOString();
   const updatedAt = insertedAt;
   const checkData = errorDataHandler(name, year, author, summary, publisher, pageCount, readPage, reading);

   if (!checkData.statusError) {
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
      message: checkData.errorMessage
   });
   response.code(400);

   return response;
};

const getAllBooksHandler = () => ({
   status: 'success',
   data: {
      books,
   }
});
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

const changeBookHandler = () =>{

}



const errorDataHandler = (name='', year=null, author='', summary='', publisher='', pageCount=null, readPage=null, reading=null) =>{
   let statusError = true;
   let onError = '';
    
   if (name === '') {
      onError = 'name';
   } else if (year === null) {
      onError = 'year';
   } else if (author === ''){
      onError = 'author';
   } else if (summary === '') {
      onError = 'summary';
   } else if (publisher === '') {
      onError = 'publisher';
   } else if (pageCount === null) {
      onError = 'pageCount';
   } else if (readPage === null) {
      onError = 'readPage';
   } else if (reading === null) {
      onError = 'reading';
   } else{
      statusError = !statusError;
   }
   if (!statusError) {
      if (pageCount < readPage) {
         onError = 'readPage tidak boleh lebih besar dari pageCount';
         statusError = !statusError;
      }
   } else {
      onError = `Mohon isi ${onError} buku`;
   }

   const errorMessage = statusError? `Gagal menambahkan buku. ${onError}`: null;
   return{ statusError, errorMessage };
};

export {addBooksHandler, getAllBooksHandler, getBookByIdHandler};