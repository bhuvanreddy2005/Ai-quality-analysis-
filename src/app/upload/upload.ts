import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CropService } from '../crop';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './upload.html',
  styleUrl: './upload.css'
})
export class UploadComponent {

  selectedFile!: File;
  result: any;

  constructor(private cropService: CropService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  upload() {
    this.cropService.analyze(this.selectedFile)
      .subscribe(res => {
        this.result = res;
      });
  }
}
