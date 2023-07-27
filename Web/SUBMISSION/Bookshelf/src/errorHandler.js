class BooksRequestHandler{
   constructor(name='', year=null, author='', summary='', publisher='', pageCount=null, readPage=null, reading=null){
      this.name = name;
      this.year = year;
      this.author = author;
      this.summary = summary;
      this.publisher = publisher;
      this.pageCount = pageCount;
      this.readPage = readPage;
      this.reading = reading;
   }

   init(bookRequest) {
      return{
         onProcess: (processMessage) =>{
            let { status, onError } = requiredData(bookRequest);
      
            if (!status) {
               if (this.pageCount < this.readPage) {
                  onError = 'readPage tidak boleh lebih besar dari pageCount';
                  status = !status;
               }
            } else {
               onError = `Mohon isi ${onError} buku`;
            }
         
            const message = status? `Gagal ${processMessage} buku. ${onError}`: null;
            return{ status, message };
         }
      }
   }
}

function errorDataHandler(processMessage, name, year, author, summary, publisher, pageCount, readPage, reading) {
   const bookRequest = new BooksRequestHandler(name, year, author, summary, publisher, pageCount, readPage, reading);
   return bookRequest.init(bookRequest).onProcess(processMessage);
   
}

function requiredData(book){
    let status = true;
    let onError = '';
     
    if (book.name === '') {
       onError = 'nama';
    } else if (book.year === null) {
       onError = 'tahun';
    } else if (book.author === ''){
       onError = 'penulis';
    } else if (book.summary === '') {
       onError = 'ringkasan';
    } else if (book.publisher === '') {
       onError = 'penerbit';
    } else if (book.pageCount === null) {
       onError = 'jumlah halaman';
    } else if (book.readPage === null) {
       onError = 'halaman dibaca';
    } else if (book.reading === null) {
       onError = 'reading';
    } else{
       status = !status;
    }

   return{ status, onError };
 };

 export {errorDataHandler};