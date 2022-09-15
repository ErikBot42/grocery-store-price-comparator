window.addEventListener('load', ()=>{
    console.log('Page Loaded');
    let button = document.querySelector("button");
    button.addEventListener('click',()=>{
        console.log("Button Clicked");
        let Username = document.querySelector('#Username');
        let Password = document.querySelector('#Password');
        console.log(Username.value + " " + Password.value)
        event.preventDefault()
        if(Username.value === 'admin' && Password.value === 'password'){
            console.log('Correct login')
            window.location.assign("adminview.html");
        }
    })
    
})