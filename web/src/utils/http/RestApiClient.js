//let  ls = Window.localStorage;

 function performRequest(request, callback){


    let token = window.localStorage.getItem("token");
    if( token != null)
    {
    request.headers.append("Authorization", "Bearer "+token);
 }
  
   // request.headers.set("A", "MED "+token);
     fetch(request)
     .then(
        function(response) {
            if (response.ok) {
                // response.headers.forEach((val,key)=>{//specialconsole.log("*****"+key+" "+val);});
                 //for (var pair of response.headers.entries()) {
                   // //specialconsole.log(pair[0]+ ': '+ pair[1]);
                // }              
                 const contentType = response.headers.get("content-type");

                if(response!==null&& contentType && contentType.includes("application/json"))
                {                    
                    //specialconsole.log("Raspuns " + JSON.stringify(response));
                    response.json().then(json => callback(json, response.status,null));
                }
                else{
                    callback(response.body, response.status,null);
                }
            }
            else {
                const contentType = response.headers.get("content-type");

                if(response!==null&& contentType && contentType.includes("application/json"))
                {
                    //specialconsole.log("Raspuns " + JSON.stringify(response));
                    response.json().then(err => callback(null, response.status,  err));
                }
                else{
                    callback(null, response.status,response.body);

                }
            }
        })
    .catch(function (err) {
        //catch any other unexpected error, and set cusltom code for error = 1
        callback(null, 1, err)
    });
}


module.exports = {
    performRequest
};
