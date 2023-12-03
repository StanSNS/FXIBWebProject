export const getCloudName = () => {
    return "dazec0lqe";
}

export const getLeftAboutImagePublicID = () => {
    return "FXIBImageContent/bgw5ogxy9mgs8n2w44ow";
}

export const getRightAboutImagePublicID = () => {
    return "FXIBImageContent/jnzf9fehce8sra0wtyhq";
}

const getAdmiralMarketsImagePublicID = () => {
    return "FXIBImageContent/zmh5sjy4ffv5zy7evgkn";
}

const getBDSwissImagePublicID = () => {
    return "FXIBImageContent/ru5i7cl9un340xi9sji6";
}

const getFTMOImagePublicID = () => {
    return "FXIBImageContent/nwplqeldbdydc3s5jkkk";
}

const getICMarketsImagePublicID = () => {
    return "FXIBImageContent/veix1gqsvoycrdjpjzbr";
}

const getMyForexFundsImagePublicID = () => {
    return "FXIBImageContent/s8bephrpkg6ipa21sduu";
}

const getTrueForexFundsImagePublicID = () => {
    return "FXIBImageContent/dmiiixl85bhvizncnvw9";
}

export const getPartnerImageByPartnerTitle = (currentTitle) => {
    switch (currentTitle){
        case "Admiral Markets": return getAdmiralMarketsImagePublicID();
        case "ICMarkets": return getICMarketsImagePublicID();
        case "BDSwiss": return getBDSwissImagePublicID();
        case "True Forex Funds": return getTrueForexFundsImagePublicID();
        case "My Forex Funds": return getMyForexFundsImagePublicID();
        case "FTMO": return getFTMOImagePublicID();
        default:
            break;
    }
}

export const getFooterStripeLogo = () => {
    return "FXIBImageContent/yuvqhc4gw5dne2bohy7m";
}

export const getFooterFXIBLogo = () => {
    return "FXIBImageContent/qhim7uve2jfoqd4rj3bq";
}

export const getFooterWave = () => {
    return "FXIBImageContent/mxqkigibatsyrxzirzzr";
}

export const getWaveURL = () => {
    return "FXIBImageContent/e1rb5qtsjy21udou0p3l";
}



