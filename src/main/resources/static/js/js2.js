let uploadField = document.getElementById("file");

uploadField.onchange = function () {
    if (this.files[0].size > 2097152) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'File is too big! ',
            footer: 'Max capacity 2MB',
            background: "#ebc98a"
        })
        this.value = "";
    }
    ;
};


////try {
//    let delet = document.getElementById('delete');
//    let documentName = document.getElementById('documentName');
//    delet.addEventListener('click', (e) => {
//        //alert(typeof name.value)
//        //typeof name === 'string'
//         e.preventDefault();
//      
//        
//        Swal.fire({
//            title: 'Are you sure you want to delete the document?',
//            text: "You won't be able to revert this!",
//            icon: 'warning',
//            showCancelButton: true,
//            confirmButtonColor: '#3085d6',
//            cancelButtonColor: '#d33',
//            confirmButtonText: 'Yes, delete it!'
//        }).then((result) => {
//            if (result.isConfirmed) {
//                Swal.fire(
//                        'Deleted!',
//                        'Your file has been deleted.',
//                        'success'
//                        );
//                //flag=true;
//                setTimeout(() => {
//                
//                   
//                }, 2000);
//            }
//        });
//       
//
//    });
//
//} catch (e) {
//}


