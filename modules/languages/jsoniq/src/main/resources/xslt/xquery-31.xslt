<?xml version="1.0" encoding="UTF-8"?>
<!--
    بسم الله الرحمن الرحيم
   
    In the name of Allah, the Most Compassionate, the Most Merciful
   
    This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java"
	exclude-result-prefixes="t"
	xmlns:t="io.dgms.unity.modules.languages.jsoniq.compiler.util.StringEscape">
	<xsl:output method="text" />

	<xsl:template match="QuantifiedExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="op" select="terminal[1]" />
				<xsl:choose>
					<xsl:when test="$op='some'">
						<xsl:text>_op:symbolic-some(for </xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>_op:symbolic-every(for </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:for-each select="QuantifiedVarDecl">
					<xsl:apply-templates>
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
					<xsl:if test="position() &lt; last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:text> return </xsl:text>
				<xsl:apply-templates
					select="*[not(self::terminal|self::QuantifiedVarDecl)][1]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="AdditiveExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:variable name="count" select="count($nodes)" />
				<xsl:for-each select="$nodes">
					<xsl:sort select="position()" data-type="number" order="descending" />
					<xsl:if test="position() mod 2 = 0">
						<xsl:choose>
							<xsl:when test=".='+'">
								<xsl:text>_op:symbolic-add(</xsl:text>
							</xsl:when>
							<xsl:when test=".='-'">
								<xsl:text>_op:symbolic-subtract(</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>ERROR</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
				<xsl:for-each select="$nodes">
					<xsl:if test="position() mod 2 = 1">
						<xsl:apply-templates select=".">
							<xsl:with-param name="symbolic-module" select="$symbolic-module" />
							<xsl:with-param name="symbolic-context" select="$symbolic-context" />
						</xsl:apply-templates>
						<xsl:choose>
							<xsl:when test="position()=1">
								<xsl:text>, </xsl:text>
							</xsl:when>
							<xsl:when test="position()=$count">
								<xsl:text>)</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>), </xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="MultiplicativeExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:variable name="count" select="count($nodes)" />
				<xsl:for-each select="$nodes">
					<xsl:sort select="position()" data-type="number" order="descending" />
					<xsl:if test="position() mod 2 = 0">
						<xsl:choose>
							<xsl:when test="text()='*'">
								<xsl:text>_op:symbolic-multiply(</xsl:text>
							</xsl:when>
							<xsl:when test="text()='div'">
								<xsl:text>_op:symbolic-divide(</xsl:text>
							</xsl:when>
							<xsl:when test="text()='idiv'">
								<xsl:text>_op:symbolic-integer-divide(</xsl:text>
							</xsl:when>
							<xsl:when test="text()='mod'">
								<xsl:text>_op:symbolic-mod(</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>ERROR</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
				<xsl:for-each select="$nodes">
					<xsl:if test="position() mod 2 = 1">
						<xsl:apply-templates select=".">
							<xsl:with-param name="symbolic-module" select="$symbolic-module" />
							<xsl:with-param name="symbolic-context" select="$symbolic-context" />
						</xsl:apply-templates>
						<xsl:choose>
							<xsl:when test="position()=1">
								<xsl:text>, </xsl:text>
							</xsl:when>
							<xsl:when test="position()=$count">
								<xsl:text>)</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>), </xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="ComparisonExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:variable name="op" select="$nodes[2]" />
				<xsl:choose>
					<xsl:when test="$op='eq'">
						<xsl:text>_op:symbolic-value-equal(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='='">
						<xsl:text>_op:symbolic-general-equal(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='ne'">
						<xsl:text>_op:symbolic-value-not-equal(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='!='">
						<xsl:text>_op:symbolic-general-not-equal(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='lt'">
						<xsl:text>_op:symbolic-value-less-than(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='&lt;'">
						<xsl:text>_op:symbolic-general-less-than(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='gt'">
						<xsl:text>_op:symbolic-value-greater-than(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='&gt;'">
						<xsl:text>_op:symbolic-general-greater-than(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='le'">
						<xsl:text>_op:symbolic-value-less-than-or-equal(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='&lt;='">
						<xsl:text>_op:symbolic-general-less-than-or-equal(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='ge'">
						<xsl:text>_op:symbolic-value-greater-than-or-equal(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='&gt;='">
						<xsl:text>_op:symbolic-general-greater-than-or-equal(</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>ERROR</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates select="$nodes[1]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>, </xsl:text>
				<xsl:apply-templates select="$nodes[3]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="AndExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:variable name="count" select="count($nodes)" />
				<xsl:for-each select="$nodes">
					<xsl:sort select="position()" data-type="number" order="descending" />
					<xsl:if test="position() mod 2 = 0">
						<xsl:text>_op:symbolic-and(</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:for-each select="$nodes">
					<xsl:if test="position() mod 2 = 1">
						<xsl:apply-templates select=".">
							<xsl:with-param name="symbolic-module" select="$symbolic-module" />
							<xsl:with-param name="symbolic-context" select="$symbolic-context" />
						</xsl:apply-templates>
						<xsl:choose>
							<xsl:when test="position()=1">
								<xsl:text>, </xsl:text>
							</xsl:when>
							<xsl:when test="position()=$count">
								<xsl:text>)</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>), </xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- 
	<xsl:template match="InstanceofExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />
		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:variable name="count" select="count($nodes)" />
				<xsl:text>_op:symbolic-instance-of(</xsl:text>
				<xsl:apply-templates select="$nodes[1]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>, "</xsl:text>
				<xsl:value-of select="$nodes[4]" />
				<xsl:text>")</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
 -->
	<xsl:template match="OrExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:variable name="count" select="count($nodes)" />
				<xsl:for-each select="$nodes">
					<xsl:sort select="position()" data-type="number" order="descending" />
					<xsl:if test="position() mod 2 = 0">
						<xsl:text>_op:symbolic-or(</xsl:text>
					</xsl:if>
				</xsl:for-each>
				<xsl:for-each select="$nodes">
					<xsl:if test="position() mod 2 = 1">
						<xsl:apply-templates select=".">
							<xsl:with-param name="symbolic-module" select="$symbolic-module" />
							<xsl:with-param name="symbolic-context" select="$symbolic-context" />
						</xsl:apply-templates>
						<xsl:choose>
							<xsl:when test="position()=1">
								<xsl:text>, </xsl:text>
							</xsl:when>
							<xsl:when test="position()=$count">
								<xsl:text>)</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>), </xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="IfExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:text>_op:symbolic-if(</xsl:text>
				<xsl:apply-templates select="$nodes[3]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>, </xsl:text>
				<xsl:apply-templates select="$nodes[6]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>, </xsl:text>
				<xsl:apply-templates select="$nodes[8]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="NotExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:text>_op:symbolic-not(</xsl:text>
				<xsl:apply-templates select="$nodes[2]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="UnaryExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:variable name="nodes" select="*[not(@type='whitespace')]" />
				<xsl:variable name="op" select="$nodes[1]/text()" />
				<xsl:choose>
					<xsl:when test="$op='+'">
						<xsl:text>_op:symbolic-unary-plus(</xsl:text>
					</xsl:when>
					<xsl:when test="$op='-'">
						<xsl:text>_op:symbolic-unary-minus(</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>ERROR</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates select="*[not(@type='whitespace')][2]">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template
		match="FunctionCall[starts-with(terminal[@name='EQName'][1],'fn:')]">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:value-of
					select="concat('_op:', substring-after(terminal[@name='EQName'][1], 'fn:'))" />
				<xsl:apply-templates select="ArgumentList">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template
		match="FunctionCall[starts-with(terminal[@name='EQName'][1],'math:')]">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-context">
				<xsl:value-of
					select="concat('_op:', substring-after(terminal[@name='EQName'][1], 'math:'))" />
				<xsl:apply-templates select="ArgumentList">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="FunctionCall">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="not($symbolic-context) and $symbolic-module">
				<xsl:variable name="name" select="FunctionName[1]/terminal/text()" />
				<xsl:choose>
					<xsl:when test="starts-with($name, 'reflection:')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:when test="starts-with($name, 'fetch:')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:when test="starts-with($name, 'jn:')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:when test="starts-with($name, 'r:')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:when test="starts-with($name, 'functx:')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:when test="starts-with($name, 'sc:')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:when test="starts-with($name, 'dgal:')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:when test="starts-with($name, '_')">
						<xsl:value-of select="$name" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat($name, '-')" />
					</xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates select="ArgumentList">
					<xsl:with-param name="symbolic-module" select="$symbolic-module" />
					<xsl:with-param name="symbolic-context" select="$symbolic-context" />
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="terminal[@type='whitespace']">
		<xsl:value-of select="text()" />
	</xsl:template>

	<xsl:template match="AnnotatedDecl[FunctionDecl]">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="$symbolic-module">
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="true()" />
					</xsl:apply-templates>
				</xsl:copy>
				<xsl:text>;</xsl:text>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="false()" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="node()">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="FunctionDecl">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:copy>
			<xsl:apply-templates select="node()">
				<xsl:with-param name="symbolic-module" select="$symbolic-module" />
				<xsl:with-param name="symbolic-context" select="$symbolic-context" />
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="FunctionName">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test=".='parameter' or .='variable'">
				<xsl:value-of select="concat('_:', .)" />
			</xsl:when>
			<xsl:when test="not($symbolic-context) and $symbolic-module">
				<xsl:value-of select="concat(., '-')" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="." />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="ObjectConstructor">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:text>map </xsl:text>
		<xsl:copy>
			<xsl:apply-templates select="node()">
				<xsl:with-param name="symbolic-module" select="$symbolic-module" />
				<xsl:with-param name="symbolic-context" select="$symbolic-context" />
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="PairConstructor">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:variable name="key" select="*[not(@type='whitespace')][1]" />
		<xsl:variable name="value" select="*[not(@type='whitespace')][3]" />
		<xsl:choose>
			<xsl:when test="not(starts-with($key, '&quot;'))">
				<xsl:text>"</xsl:text>
				<xsl:value-of select="t:escape(t:new(),$key)" />
				<xsl:text>":</xsl:text>
				<xsl:copy>
					<xsl:apply-templates select="$value">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="t:escape(t:new(),$key)" />
				<xsl:text>:</xsl:text>
				<xsl:copy>
					<xsl:apply-templates select="$value">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="ObjectLookup">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:text>?</xsl:text>
		<xsl:choose>
			<xsl:when test="count(terminal) &gt; 1">
				<xsl:value-of select="terminal[2]" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates
						select="StringLiteral | NCName | ParenthesizedExpr | VarRef | ContextItemExpr">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text></xsl:text>
	</xsl:template>

	<xsl:template match="ArrayConstructor">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:text>array { </xsl:text>
		<xsl:copy>
			<xsl:apply-templates select="*[not(@type='whitespace')][2]">
				<xsl:with-param name="symbolic-module" select="$symbolic-module" />
				<xsl:with-param name="symbolic-context" select="$symbolic-context" />
			</xsl:apply-templates>
		</xsl:copy>
		<xsl:text> }</xsl:text>
	</xsl:template>

	<xsl:template match="ArrayUnboxing">
		<xsl:text>?*</xsl:text>
	</xsl:template>

	<xsl:template match="ContextItemExpr">
		<xsl:text>.</xsl:text>
	</xsl:template>

	<xsl:template match="BlockExpr">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:choose>
			<xsl:when test="StatementsAndOptionalExpr/*[not(self::Statements)]">
				<xsl:text>
			</xsl:text>
				<xsl:copy>
					<xsl:apply-templates
						select="StatementsAndOptionalExpr/*[not(self::Statements)]">
						<xsl:with-param name="symbolic-module" select="$symbolic-module" />
						<xsl:with-param name="symbolic-context" select="$symbolic-context" />
					</xsl:apply-templates>
				</xsl:copy>
				<xsl:text>
			</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>map {}</xsl:text>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<xsl:template match="Predicate[ArrayConstructor]">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:text>?(</xsl:text>
		<xsl:copy>
			<xsl:apply-templates select="ArrayConstructor/*[not(self::terminal)]">
				<xsl:with-param name="symbolic-module" select="$symbolic-module" />
				<xsl:with-param name="symbolic-context" select="$symbolic-context" />
			</xsl:apply-templates>
		</xsl:copy>
		<xsl:text>)</xsl:text>
	</xsl:template>

	<xsl:template match="JSONSimpleObjectUnion">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:text>map:merge(</xsl:text>
		<xsl:copy>
			<xsl:apply-templates select="*[not(self::terminal)]">
				<xsl:with-param name="symbolic-module" select="$symbolic-module" />
				<xsl:with-param name="symbolic-context" select="$symbolic-context" />
			</xsl:apply-templates>
		</xsl:copy>
		<xsl:text>)</xsl:text>
	</xsl:template>

	<xsl:template match="EQName[terminal='array']">
		<xsl:text>array(*)</xsl:text>
	</xsl:template>

	<xsl:template match="FunctionName[terminal='object']">
		<xsl:text>map(*)</xsl:text>
	</xsl:template>

	<xsl:template match="JSONItemTest">
		<xsl:text>item()</xsl:text>
	</xsl:template>

	<xsl:template match="FunctionName[terminal='integer']">
		<xsl:text>xs:integer</xsl:text>
	</xsl:template>

	<xsl:template match="FunctionName[terminal='double']">
		<xsl:text>xs:double</xsl:text>
	</xsl:template>

	<xsl:template match="FunctionName[terminal='decimal']">
		<xsl:text>xs:decimal</xsl:text>
	</xsl:template>

	<xsl:template match="FunctionName[terminal='string']">
		<xsl:text>xs:string</xsl:text>
	</xsl:template>

	<xsl:template match="FunctionName[terminal='boolean']">
		<xsl:text>xs:boolean</xsl:text>
	</xsl:template>

	<xsl:template match="FunctionName[terminal='atomic']">
		<xsl:text>xs:anyAtomicType</xsl:text>
	</xsl:template>



	<xsl:template match="Literal">
		<xsl:value-of select="t:escape(t:new(),.)" />
	</xsl:template>

	<xsl:template match="StringLiteral">
		<xsl:value-of select="t:escape(t:new(),.)" />
	</xsl:template>

	<xsl:template match="BooleanLiteral">
		<xsl:value-of select="concat(., '()')" />
	</xsl:template>


	<xsl:template match="JSONObjectTest">
		<xsl:text>map(*)</xsl:text>
	</xsl:template>

	<xsl:template match="JSONArrayTest">
		<xsl:text>array(*)</xsl:text>
	</xsl:template>

	<xsl:template match="ModuleImport[terminal/text()='at']">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:apply-templates
			select="*[not(text()='at') and not(preceding-sibling::terminal[text()='at'])]">
			<xsl:with-param name="symbolic-module" select="$symbolic-module" />
			<xsl:with-param name="symbolic-context" select="$symbolic-context" />
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="VersionDecl">
	</xsl:template>

	<xsl:template match="ModuleDecl">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:copy>
			<xsl:apply-templates select="node()">
				<xsl:with-param name="symbolic-module" select="$symbolic-module" />
				<xsl:with-param name="symbolic-context" select="$symbolic-context" />
			</xsl:apply-templates>
		</xsl:copy>
		<xsl:if test="$symbolic-module">
			<xsl:text>import module namespace _op = "http://dgms.io/unity/modules/symbolics/jsoniq/operators";</xsl:text>
		</xsl:if>
	</xsl:template>

	<xsl:template match="/">
		<xsl:text>xquery version "3.1";</xsl:text>
		<xsl:choose>
			<xsl:when
				test="contains(normalize-space(//OptionDecl), 'symbolic-computation &quot;disabled&quot;')">
				<xsl:apply-templates>
					<xsl:with-param name="symbolic-module" select="false()" />
					<xsl:with-param name="symbolic-context" select="false()" />
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="not(//Module/LibraryModule)">
					<xsl:text>import module namespace _op = "http://dgms.io/unity/modules/symbolics/jsoniq/operators";</xsl:text>
				</xsl:if>
				<xsl:apply-templates>
					<xsl:with-param name="symbolic-module" select="true()" />
					<xsl:with-param name="symbolic-context" select="true()" />
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="node()">
		<xsl:param name="symbolic-module" select="false()" />
		<xsl:param name="symbolic-context" select="false()" />

		<xsl:copy>
			<xsl:apply-templates select="node()">
				<xsl:with-param name="symbolic-module" select="$symbolic-module" />
				<xsl:with-param name="symbolic-context" select="$symbolic-context" />
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
