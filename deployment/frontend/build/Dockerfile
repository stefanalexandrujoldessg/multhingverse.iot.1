FROM nginx:stable-alpine

USER root

RUN rm /etc/nginx/nginx.conf

ADD nginx.conf /etc/nginx

ADD build /usr/share/nginx/html/
ADD build /var/www/html
