FROM node:16-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install

# Add the missing Babel plugin 
RUN npm install --save-dev @babel/plugin-proposal-private-property-in-object 

# Copy the rest of your application code COPY . .
COPY . .

RUN npm run build


EXPOSE 3000


# Start the development server
CMD ["npm", "start"]