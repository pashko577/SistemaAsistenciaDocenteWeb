import { HttpEvent, HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Token } from '../services/token';
import { Observable } from 'rxjs';

export const AuthInterceptor: HttpInterceptorFn = (req, next)=>{
  const tokenService = inject(Token);
  const token = tokenService.getToken();

if (token){
  req = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });
}
  return next(req);
}