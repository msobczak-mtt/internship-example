db.createUser({
    user: 'admin',
    pwd: 'admin',
    roles: [
        {
            role: 'readWrite',
            db: 'lab',
        },
    ],
});

db.createCollection('rating', {capped: true, size: 100000});
