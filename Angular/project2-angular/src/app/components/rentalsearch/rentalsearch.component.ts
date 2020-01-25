import { Component, OnInit } from '@angular/core';
import { PropertyService } from 'src/app/services/property.service';
import { Preference } from 'src/app/model/preference';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SessionService } from 'src/app/services/session.service';
import { Property } from 'src/app/model/property';

@Component({
  selector: 'app-rentalsearch',
  templateUrl: './rentalsearch.component.html',
  styleUrls: ['./rentalsearch.component.css']
})
export class RentalsearchComponent implements OnInit {
  response: any;
  //readonly APP_URL = 'http://localhost:8080/RevatureRealocator'; //not sure if this is right
  preference: Preference;
  submitted = false;
  simple = true;
  prefForm: FormGroup;

  constructor(private propertyService: PropertyService,
    private router: Router,
    private sessionService: SessionService,
    private fb: FormBuilder) {
      this.createForm();
  }
  
  createForm(){
    this.prefForm = this.fb.group({
      city: ['', Validators.required],
      state_code: ['', Validators.required],
      min_price: ['', [Validators.required, Validators.min(0)]],
      max_price: ['', [Validators.required, Validators.min(0), Validators.max(50000)]],
      num_beds: ['', Validators.min(0)],
      num_baths: ['', Validators.min(0)],
    });
  }

  ngOnInit() {  
  }

  doSearch(){
    this.preference = new Preference(this.prefForm.value);
    this.preference.state_code = this.preference.state_code.toUpperCase();
    console.log(this.preference);
    this.propertyService.getPropertiesByPref(this.preference).subscribe(
      data => {
        if(data != null){
            this.response = data;
            console.log('Found properties');
        }
      }, error => {
          console.log('Error ', error);
        }
      )
    this.prefForm.reset();
  }

  
    searchWithSavedPref(){
      console.log(this.sessionService.getStoredPreference());
      this.preference = this.sessionService.getStoredPreference();
      this.propertyService.getPropertiesByPref(this.preference).subscribe(
        data => {
          if(data != null){
              this.response = data;
              console.log('Found properties');
          }
        }, error => {
            console.log('Error ', error);
          }
        )
      this.prefForm.reset();
      // if(this.preference.max_price == null && this.preference.num_baths == null && this.preference.num_beds == null){
      //   this.propertyService.getPropertiesSimple(this.preference).subscribe(
      //     data => {
      //       if(data != null) {
      //         this.response = data;
      //       }
      //     }, error => {
      //       console.log('Error', error);
      //     }
      //   )
      // } else {
      //   this.propertyService.getPropertiesByPref(this.preference).subscribe(
      //     data => {
      //       if(data != null){
      //         this.response = data;
      //       }
      //     }, error => {
      //       console.log('Error', error);
      //     }
      //   )
      // }
    }

    goToIndividualPropertyPage(property: Property){
      this.propertyService.openPropertyPage(property); //Saves property within propertyservice so that individual property page can pull that information
      this.router.navigateByUrl('/individualpropertypage');
    }
  }
