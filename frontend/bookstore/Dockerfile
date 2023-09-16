FROM alpine

EXPOSE 88

ENV PATH="$PATH:/usr/bin/python3"

ADD config/default.conf /etc/nginx/http.d/default.conf

COPY . /var/www/localhost/htdocs

RUN apk add --no-cache nginx nodejs python3 npm && \
    cd /var/www/localhost/htdocs && \
    npm install --no-cache && \
    npm run build && \
    apk del nodejs && \
    apk del npm && \
    mv /var/www/localhost/htdocs/build /var/www/localhost && \
    cd /var/www/localhost/htdocs && \
    rm -rf * && \
    mv /var/www/localhost/build /var/www/localhost/htdocs;

CMD ["/bin/sh", "-c", "exec nginx -g 'daemon off;';"]

WORKDIR /var/www/localhost/htdocs