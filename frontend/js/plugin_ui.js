var background = chrome.extension.getBackgroundPage();
var colors = {
    "-1":"#408964",
    "0":"#f2de38",
    "1":"#ff8b66"
};
var featureList = document.getElementById("features");

chrome.tabs.query({ currentWindow: true, active: true }, function(tabs){
    var result = background.results[tabs[0].id];
    var isPhish = background.isPhish[tabs[0].id];
    var legitimatePercent = background.legitimatePercents[tabs[0].id];

    /*for(var key in result){
        var newFeature = document.createElement("li");
        //console.log(key);
        newFeature.textContent = key;
        //newFeature.className = "rounded";
        newFeature.style.backgroundColor=colors[result[key]];
        featureList.appendChild(newFeature);
    }*/
    
    $("#site_score").text("Safe");
    if(isPhish) {
        $("#res-circle").css("background", "#ff8b66");
        $("#site_msg").text("Warning!! Phishing Website");
        $("#site_score").text("Phishing");
        if(parseInt(legitimatePercent)-20>60){
            $("#res-circle").css("background", "#f2de38");
            $("#site_msg").text("Suspicious Link!! Use on your own risk");
            $("#site_score").text("Suspicious");
        }
    }
});

$('body').on('click', 'a[target="_parent]', function(e){
    e.preventDefault();
    chrome.tabs.create({url: $(this).prop('href'), active: false});
    return false;
});

