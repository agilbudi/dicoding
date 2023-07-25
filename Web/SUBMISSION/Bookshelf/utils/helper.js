class Helper{
    constructor(message=null, data=null){
        this.message = message;
        this.data = data;
    }


    get(){
        return mGet(this.message, this.data);
    }
    post(){
        return mPost(this.message, this.data);
    }
    
    responseMessage(){
        return{
            successMessage: () =>{
                if (this.message != null) {
                    return `"status": "success", "message": "${this.message}", "data": ${this.data}`;
                } else {
                    return `"status": "success", "data": ${this.data}`;
                }
            },
            errorMessage: () =>{
                return `"status": "fail", "message": "${this.message}"`;
            }
        }
    }
}
function mGet(message, data) {
    let statusCode = 410;
    const helper = new Helper();
    try {
        if (data == null) {
            statusCode = 404;
            throw new ReferenceError("Data Not Found!");
        }else{
            statusCode = 200;
            helper.data = data;
            helper.message = message;
            const getMessage = helper.responseMessage(helper).successMessage();
            return {
                statusCode, getMessage
            };
        }
    } catch (error) {
        console.log(`====================================> error code: ${statusCode}`);
        helper.statusCode = statusCode;
        if (error instanceof SyntaxError) {
            console.log(`====================================> JSON Syntax Error: ${error.message}`);
            helper.message = error.message;
        } else if (error instanceof ReferenceError) {
            console.log(`====================================> ${error.message}`);
            helper.message = error.message;
        } else {
            console.log(`====================================> ${error.stack}`);
            helper.message = error.stack.toString();
        }
        const getMessage = helper.responseMessage(helper).errorMessage();
        return{
            statusCode, getMessage
        }
    }
}
function mPost(message, data) {
    let statusCode = 410;
    const helper = new Helper();
    //jika gagal muncul pesan apa jika berhasil muncul pesan apa
    try {
        if (!data) {
            //jika error
            statusCode = 400;
            throw new SyntaxError(message);
        }else{
            statusCode = 201;
            helper.data = data;
            helper.message = message;
            const getMessage = helper.responseMessage(helper).successMessage();
            return {
                statusCode, getMessage
            };
        }
    } catch (error) {
        console.log(`====================================> error code: ${statusCode}`);
        helper.statusCode = statusCode;
        if (error instanceof SyntaxError) {
            console.log(`====================================> ${error.message}`);
            helper.message = error.message;
        } else if (error instanceof ReferenceError) {
            console.log(`====================================> ${error.message}`);
            helper.message = error.message;
        } else {
            console.log(`====================================> ${error.stack}`);
            helper.message = error.stack.toString();
        }
        const getMessage = helper.responseMessage(helper).errorMessage();
        return{
            statusCode, getMessage
        }
    }
    
}

export {Helper};

/*

curl -X GET http://localhost:9000/books -i
curl -X POST -H "Content-Type: application/json" http://localhost:9000/books -d "{\"name\": \"Buku A\", \"year\": 2010, \"author\": \"John Doe\", \"summary\": \"Lorem ipsum dolor sit amet\", \"publisher\": \"Dicoding Indonesia\", \"pageCount\": 100, \"readPage\": 25, \"reading\": false}" -i
*/