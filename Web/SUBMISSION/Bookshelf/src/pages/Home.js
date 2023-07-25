import { Helper } from '../../utils/helper.js';
import { Books } from "../model.js";
import { nanoid } from "nanoid";

const home = function(request, response){
    const { method } = request;
    const methodHelper = new Helper();
    let message = 'Gone !!!';
    let data = [];//["{\"Roxy\", \"Anna\", \"Ricco\", \"Ronaldo\",\"Mario\", \"Elisabeth\"}"];
    let newData = [];

    switch (method) {
        case 'GET':
            if (!data) {
                message = 'Tidak ada data!!!!';
            }else{
                message = null;
                methodHelper.message = message;
                methodHelper.data = data;
            }
            const getMethod = methodHelper.get();
            console.log(getMethod.getMessage);
            response.statusCode = getMethod.statusCode;
            response.end(`<h1>${getMethod.getMessage}</h1>`);
            break;

        case 'POST':
            request.on('data', (chunk) =>{
                data.push(chunk);
            });
            request.on('end', ()=>{
                data = Buffer.concat(data).toString();
                const date = new Date().toISOString();
                const books = new Books();
                const bookId = nanoid();
                const { 
                    name, year, author, summary, publisher, pageCount, readPage, reading
                } = JSON.parse(data);

                const cekData = getErrorData(name, year, author, summary, publisher, pageCount, readPage, reading);
                if (!cekData.status) {
                    books.id = bookId;
                    books.name = name;
                    books.year = year;
                    books.author = author;
                    books.summary = summary;
                    books.publisher = publisher;
                    books.pageCount = pageCount;
                    books.readPage = readPage;
                    books.finished = (pageCount === readPage)? true: false;
                    books.reading = reading;
                    books.insertedAt = date;
                    books.updateAt = date;
                    newData.push(books);
                    
                    message = 'Buku berhasil ditambahkan';
                    methodHelper.data = `{"bookId": "${books.id}"}`;
                    methodHelper.message = message;
                }else{
                    message = cekData.errorMessage;
                    newData = null;
                    methodHelper.data = newData;
                    methodHelper.message = message;
                }

                const postMethod = methodHelper.post();
                response.statusCode = postMethod.statusCode;
                response.end(`<h1>${postMethod.getMessage}</h1>`);
            });
            break;
    
        default:
            response.statusCode = 410;
            response.end('<h1>Halaman tidak ditemukan!</h1>');
            break;
    }
    
}

function getErrorData(name='', year=null, author='', summary='', publisher='', pageCount=null, readPage=null, reading=null) {
    let onError = null;
    let message = `Mohon isi ${onError} buku`;
    
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
     }
     if(readPage > pageCount){
        onError = '';
        message = 'readPage tidak boleh lebih besar dari pageCount';
     }else{
        message = `Mohon isi ${onError} buku`;
     }

    const status = (onError === null)? false: true;
    const errorMessage = status? `Gagal menambahkan buku. ${message}`: null;

     return {status,  errorMessage};
}

export {home};