const minLengthValidator = (value, minLength) => {
    return value.length >= minLength;
};

const requiredValidator = value => {
    return value.trim() !== '';
};
/*
function emailValidator(mail) 
{
 if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(myForm.emailAddr.value))
  {
    return (true)
  }
    alert("You have entered an invalid email address!")
    return (false)
}
*/
 
const passwordValidator = value => {
    if (/^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,16}$/.test(value))
  {
    return true;
  }
     return false;
}


const validate = (value, rules) => {
    let isValid = true;

    for (let rule in rules) {

        switch (rule) {
            case 'minLength': isValid = isValid && minLengthValidator(value, rules[rule]);
                              break;

            case 'isRequired': isValid = isValid && requiredValidator(value);
                               break;

            case 'isPassword': isValid = isValid && passwordValidator(value);
            break;

            

            default: isValid = true;
        }

    }

    return isValid;
};

export default validate;
