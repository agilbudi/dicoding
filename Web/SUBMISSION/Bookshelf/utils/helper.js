class MHelper{
    constructor(message=null, data=null){
        this.message = message;
        this.data = data;
    }


    get(){
        return mGet(this.message, this.data);
    }
    
    getMessage(){
        return{
            successMessage: () =>{
                if (this.message != null) {
                    return `Status: success \nMessage: ${this.message}\nData: ${this.data}`;
                } else {
                    return `Status: success \nData: ${this.data}`;
                }
            },
            errorMessage: () =>{
                return `Status: fail \nMessage: ${this.message}`;
            },
            default: () =>{
                return 'ini testResult';
            }
        }
    }
}
function mGet(message, data) {
    let statusCode = 410;
    const mHelper = new MHelper();
    try {
        if (data == null) {
            statusCode = 404;
            throw new ReferenceError("Data Not Found!");
        }else{
            statusCode = 200;
            mHelper.data = data;
            mHelper.message = message;
            const getMessage = mHelper.getMessage(mHelper).successMessage();
            return {
                statusCode, getMessage
            };
        }
    } catch (error) {
        console.log(`====================================> error code: ${statusCode}`);
        mHelper.statusCode = statusCode;
        if (error instanceof SyntaxError) {
            console.log(`====================================> JSON Syntax Error: ${error.message}`);
            mHelper.message = error.message;
        } else if (error instanceof ReferenceError) {
            console.log(`====================================> ${error.message}`);
            mHelper.message = error.message;
        } else {
            console.log(`====================================> ${error.stack}`);
            mHelper.message = error.stack.toString();
        }
        const getMessage = mHelper.getMessage(mHelper).errorMessage();
        return{
            statusCode, getMessage
        }
    }
}

module.exports = MHelper;