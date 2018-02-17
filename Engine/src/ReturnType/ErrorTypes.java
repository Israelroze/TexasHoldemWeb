package ReturnType;

public enum ErrorTypes {
    XML_FILE_ERROR{
        @Override
        public String toString() {
            return "Problem With XML FILE";
        }
    },
    BAD_INPUT{
        public String toString(){
        return "Bad Input, Please choose again";
        }
    }
}
