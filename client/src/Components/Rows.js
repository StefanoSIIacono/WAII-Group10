function ProductRow(props) {
    return (
        <>
            <tr>
                <ProductData product={props.product} />
            </tr>
        </>
    );
}

function ProductData(props) {
    return <>
        <td>{props.product.ean}</td>
        <td>{props.product.name}</td>
        <td>{props.product.brand}</td>
    </>
}

function ProfileRow(props) {
    return (
        <>
            <tr>
                <ProfileData profile={props.profile} />
            </tr>
        </>
    );
}

function ProfileData(props) {
    return <>
        <td>{props.profile.profileID}</td>
        <td>{props.profile.email}</td>
        <td>{props.profile.password}</td>
    </>
}

export {ProductRow, ProfileRow};