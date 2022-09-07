<%@ page contentType="text/html;charset=UTF-8" %> 
<!DOCTYPE html>
<html lang="ja">
	<body>
		<h2>GETのサンプル</h2>
		<%=request.getAttribute("items") %>
		
		<form method="post" action="GetSample">
			<input type="text" name="item">
			<button>登録</button>
		</form>
		
	</body>
</html>
