$(document).ready(function(){
	
	$('#logout').click(function(e){
		
		e.preventDefault();
		$('#logout-form').submit();
		console.log('inside logout func');
	});
});