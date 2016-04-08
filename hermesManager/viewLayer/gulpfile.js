//gulp
var gulp = require('gulp');

//plugins
var connect = require('gulp-connect');
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
var minifyCSS = require('gulp-minify-css');
var clean = require('gulp-clean');
var wiredep = require('wiredep').stream;
var inject = require('gulp-inject');
var runSequence = require('run-sequence');
var change = require('gulp-change');

function changeBuild(content) {
	return content.replace('%%SERVER_URL%%', 'http://localhost:8080/eventManager/');
}

function changeDist(content) {
	return content.replace('%%SERVER_URL%%', 'http://cronos.lbd.org.es/hermes/');
}

gulp.task('changeBuildTask', function() {
	return gulp.src('./app/js/app.constants.js')
	.pipe(change(changeBuild))
	.pipe(uglify({
		// inSourceMap:
		// outSourceMap: "app.js.map"
	}))
	.pipe(gulp.dest('./dist/js/'))
});

gulp.task('changeDistTask', function() {
	return gulp.src('./app/js/app.constants.js')
	.pipe(change(changeDist))
	.pipe(uglify({
		// inSourceMap:
		// outSourceMap: "app.js.map"
	}))
	.pipe(gulp.dest('./dist/js/'))
});


//tasks
gulp.task('lint', function() {
	gulp.src(['./app/**/*.js', '!./app/bower_components/**'])
	.pipe(jshint())
	.pipe(jshint.reporter('default'))
	.pipe(jshint.reporter('fail'));
});

gulp.task('clean', function() {
	return gulp.src('./dist/*')
	.pipe(clean({force: true}));
});

gulp.task('minify-css', function() {
	var opts = {comments:true,spare:true};
	gulp.src(['./app/**/*.css', '!./app/bower_components/**'])
	.pipe(minifyCSS(opts))
	.pipe(gulp.dest('./dist/'))
});

gulp.task('minify-js', function() {
	gulp.src(['./app/**/*.js', '!./app/bower_components/**', '!./app/js/app.constants.js'])
	.pipe(uglify({
		// inSourceMap:
		// outSourceMap: "app.js.map"
	}))
	.pipe(gulp.dest('./dist/'))
});

gulp.task('copy-css', function() {
	gulp.src(['./app/**/*.css', '!./app/bower_components/**'])
	.pipe(gulp.dest('./dist/'))
});

gulp.task('copy-js', function() {
	gulp.src(['./app/**/*.js', '!./app/bower_components/**', '!./app/js/app.constants.js'])
	.pipe(gulp.dest('./dist/'))
});

gulp.task('copy-bower-components', function () {
	gulp.src('./app/bower_components/**')
	.pipe(gulp.dest('dist/bower_components'));
});

gulp.task('copy-files', function () {
	gulp.src('./app/login.html').pipe(gulp.dest('dist/'));
	gulp.src('./app/partials/*.html').pipe(gulp.dest('dist/partials/'));
	gulp.src('./app/partials/**/*.html').pipe(gulp.dest('dist/partials/'));
	gulp.src('./app/translations/*.json').pipe(gulp.dest('dist/translations/'));
});

gulp.task('connect', function () {
	connect.server({
		root: 'dist/',
		port: 8888,
		livereload: true
	});
});

gulp.task('wiredep:app', function() {
	return gulp.src('./app/index.html')
	.pipe(wiredep({
		ignorePath: '../../',
		exclude: [
		          './app/bower_components' + '/bootstrap-sass/assets/javascripts/bootstrap.js'
		          ]
	}))
	.pipe(inject(gulp.src([
	                       './app/js/app.js',
	                       './app/js/**/*.js',
	                       './app/js/*.js',
	                       './app/css/*.css',
	                       './app/translations/translation_*.js',
	                       './app/translations/angular-locale_*.js'
	                       ], {
		read: false
	}), {
		relative: true
	}))
	.pipe(gulp.dest('./dist'));
});

gulp.task('reload', function () {
	return gulp.src('./dist/')
	.pipe(connect.reload());
});

gulp.task('watch', function () {
	gulp.watch(['./app/partials/*.html'], ['wiredep:app', 'reload']);
	gulp.watch(['./app/partials/**/*.html'], ['copy-files', 'reload']);
	gulp.watch(['./app/translations/*.json'], ['copy-files', 'reload']);
	gulp.watch(['./app/js/*.js'], ['copy-js', 'reload']);
	gulp.watch(['./app/css/*.css'], ['copy-css', 'reload']);
});

//default task
gulp.task('default',
		['lint','wiredep:app','connect', 'watch']
);

gulp.task('build', function(cb) {
	runSequence(['clean'],
			['wiredep:app'], ['changeBuildTask'] ,['lint', 'copy-css', 'copy-js', 'copy-files', 'copy-bower-components'], 'connect', 'watch', cb);
});

gulp.task('buildDist', function(cb) {
	runSequence(['clean'],
			['wiredep:app'], ['changeDistTask'] ,['lint', 'minify-css', 'minify-js', 'copy-files', 'copy-bower-components'], cb);
});
