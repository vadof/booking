FROM    node:latest

WORKDIR /usr/src/app

COPY    package.json package-lock.json
COPY    . .

RUN     npm install
RUN     npm install -g @angular/cli

#EXPOSE  4200

#CMD     ["ng", "serve", "--host", "0.0.0.0"]
CMD      ["ng", "build"]

FROM    nginx:latest

COPY    nginx.conf /etc/nginx/nginx.conf
COPY    --from=build /usr/src/app/dist/* /usr/share/nginx/html
