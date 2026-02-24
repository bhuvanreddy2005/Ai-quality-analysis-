import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CropService {

  private apiUrl = 'http://localhost:8080/api/crops';

  constructor(private http: HttpClient) {}

  analyze(image: File) {
    const formData = new FormData();
    formData.append('image', image);

    return this.http.post(`${this.apiUrl}/analyze`, formData);
  }
}
