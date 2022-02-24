
//----------Change name form Validation--------------------
try {
    let name = document.getElementById('newName');

    let form = document.getElementById('form');
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        //alert(typeof name.value)
        //typeof name === 'string'
        if (name.value.trim() === "") {
            Swal.fire({
                title: "The file name can not be empty",
                text: "Try again with a different name",
                allowEscapeKey: true,
                allowEnterKey: true
            });
            // alert("The file name can not be empty");
        } else if (name.value.trim().includes(".")) {
            Swal.fire({
                title: "The file name can not have a '.' inside",
                text: "Try again with a different name",
                allowEscapeKey: true,
                allowEnterKey: true
            });
        } else {
            Swal.fire({
                icon: 'success',
                title: "The name has been succesfuly changed",
                text: "New file name: ".concat(name.value),
                showConfirmButton: false,
                timer: 3500,
                 background: "#0c0e0d"
            })
            setTimeout(() => {
                form.submit();
            }, 4000);
        }
    });

} catch (error) {
}

//----------Form Validation--------------------
try {

    let form = document.getElementById('form2');
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        Swal.fire({
            title: "You have succesfully upoladed the file!",
            text: "data2",
            timer: 3000,
            background: "#ebc98a"

        })
        setTimeout(() => {
            form.submit();
        }, 4000);
    });

} catch (error) {

}


//----------------------------------------------
//
//Swal.fire({
//    title: "WELCOME TO ARTURO'S API",
//    text: "HOPE YOU LIKE IT :)"
//            // html:
//            // icon:
//            // confirmButtonText:
//            // footer:
//            // width:
//            // padding:
//            // background:
//            // grow:
//            // backdrop:
//            // timer:
//            // timerProgressBar:
//            // toast:
//            // position:
//            // allowOutsideClick:
//            // allowEscapeKey:
//            // allowEnterKey:
//            // stopKeydownPropagation:
//
//            // input:
//            // inputPlaceholder:
//            // inputValue:
//            // inputOptions:
//
//            //  customClass:
//            // 	container:
//            // 	popup:
//            // 	header:
//            // 	title:
//            // 	closeButton:
//            // 	icon:
//            // 	image:
//            // 	content:
//            // 	input:
//            // 	actions:
//            // 	confirmButton:
//            // 	cancelButton:
//            // 	footer:	
//
//            // showConfirmButton:
//            // confirmButtonColor:
//            // confirmButtonAriaLabel:
//
//            // showCancelButton:
//            // cancelButtonText:
//            // cancelButtonColor:
//            // cancelButtonAriaLabel:
//
//            // buttonsStyling:
//            // showCloseButton:
//            // closeButtonAriaLabel:
//
//
//            // imageUrl:
//            // imageWidth:
//            // imageHeight:
//            // imageAlt:
//});
//
//
