function galleryItem(imgs) {
    let posterImg = document.getElementById("posterImg");
    let imgText = document.getElementById("imgtext");
    let expandedGallery = document.getElementById("expandedGallery");
    let title = document.getElementById("title");
    let text = document.getElementById("text");

    posterImg.src = imgs.src;
    imgText.innerHTML = imgs.alt;
    title.textContent = imgs.alt;
    expandedGallery.parentElement.style.display = "block";
    switch (imgs.alt) {
        case "Latihan Kader":
            text.textContent = "Berlatih kemampuan kepemimpinan dan memperkenalkan cara berorganisasi menyampaikan pendapat dan melatih penyelesaian masalah dalam kelompok."
            break;
        case "Rihlah":
          text.textContent = "Mengenal alam sebagaimana Allah telah sediakan untuk manusia, mengenal alam agar dapat memanfaatkan semestinya sehingga tidak merusak dan dapat merawatnya."
            break;
        case "Tabligh Akbar":
          text.textContent = "Bekerja sama dengan pihak kampus mengadakan pengajian untuk umum yang mengundang Ustadz untuk menyampaikan kajian islam yang dapat menambah iman dan semangat beribadah."
            break;
        case "MAT (Musyawarah Anggota Tahunan)":
          text.textContent = "Musyawarah yang diadakan tiap tahun, menentukan kepengurusan berikutnya, mengambil sumpah janji kepengurusan dan menetapkan peraturan atau undang-undang dalam kepengurusan UKM WAMIKA."
            break;
        case "RDK (Ramadhan di Kampus)":
          text.textContent = "Kegiatan paling asyik diwaktu bulan Ramadhan yang diadakan dilingkungan kampus dengan nama kegiatan 'Rumah Ramadhan' yang menghadirkan acara kultum ngabuburit, buka bersama, bagi-bagi sedekah, dan kegiatan berpahala lainya yang susah untuk dilupakan dalam perkuliahan di kampus STMIK AKAKOM Yogyakarta."
            break;
        case "Idul Adha (Qurban)":
          text.textContent = "Pada hari raya Idul Adha kampus selalu mengadakan qurban dan UKM WAMIKA sebagai panitia untuk mengurusi acara qurban tersebut dari pemotongan hingga pembagian daging qurban."
            break;
        default:
            break;
    }
  }