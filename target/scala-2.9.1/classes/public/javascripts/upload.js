    var form = document.getElementById('file-form');
    var fileSelect = document.getElementById('file-select');
    var uploadButton = document.getElementById('upload-button');

    form.onsubmit = function(event) {
        event.preventDefault();

        //Update button text.
        uploadButton.innerHTML = 'Uploading...';

        var files = fileSelect.files;
        var formData = new FormData();
        var file = files[0];

         // Check the file type.
         if (!file.type.match('image.*')) {
        continue;
        }

        // Add the file to the request.
        formData.append('photo', file, file.name);

        // Set up the request.
        var xhr = new XMLHttpRequest();

        // Open the connection.
        xhr.open('POST', 'handler.php', true);

        xhr.addEventListener("load", transferComplete,false);

        // Set up a handler for when the request finishes.
        xhr.onload = function () {
            if (xhr.status === 200) {

        } else {
            alert('An error occurred!');
            }
        };

        // Send the Data.
        xhr.send(formData);


}

   
