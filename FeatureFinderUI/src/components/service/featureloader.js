import { useEffect, useState } from 'react';

export default function Home() {
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        fetch('/api/hello')
            .then((res) => res.json())
            .then((data) => {
                // do something with data
            })
            .catch((err) => {
                console.log(err);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <LoadingComponent />;
    }

    return <MyRegularComponent />;
}